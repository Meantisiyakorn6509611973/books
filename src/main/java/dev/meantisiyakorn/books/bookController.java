package dev.meantisiyakorn.books;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class bookController {
    private final StorageRepository repository;
    private final BorrowHistoryRepository historyRepository;


    bookController(StorageRepository repository, BorrowHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @GetMapping("/books")
    List<Storage> findAll() {
        return repository.findAll();
    }


    @GetMapping("/books/{id}")
    Storage findOne(@PathVariable Long id) {
        Optional<Storage> book = repository.findById(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }

        // Force calculation of current holder
        Storage storage = book.get();
        storage.getCurrentHolder();

        return storage;
    }
    
    @PostMapping("/books")
    Storage newbook(@RequestBody Storage book) {        
        return repository.save(book);
    }
    
    @PutMapping("/books/{id}")
    Storage savebook(@RequestBody Storage newbook, @PathVariable Long id) {
        return repository.findById(id).map(book -> {
            String oldBookDetail = book.getBookDetail();
            String newBookDetail = newbook.getBookDetail();

            book.setBorrowed(newbook.getBorrowed());
            book.setLocationName(newbook.getLocationName());
            book.setOwnerName(newbook.getOwnerName());
            book.setBookDetail(newBookDetail); // Update book detail
            
            Storage updatedBook = repository.save(book);

            // Update bookDetail in BorrowHistory if it has changed
            if (!oldBookDetail.equals(newBookDetail)) {
                List<BorrowHistory> histories = historyRepository.findByBookId(id);
                for (BorrowHistory history : histories) {
                    history.setBookDetail(newBookDetail);
                    historyRepository.save(history);
                }
            }

            return updatedBook;
        }).orElseGet(() -> repository.save(newbook));
    }

    @PostMapping("/books/{id}/borrow")
    Storage borrowBook(@PathVariable Long id, @RequestBody Map<String, String> borrowDetails) {
        String borrowerName = borrowDetails.get("borrowerName");
        String receivedFrom = borrowDetails.get("receivedFrom"); // Who gave it to them?

        return repository.findById(id).map(book -> {
            // Ensure `receivedFrom` is either `currentHolder` or `ownerName`
            String validReceivedFrom = book.getCurrentHolder() != null ? book.getCurrentHolder() : book.getOwnerName();
            if (!validReceivedFrom.equals(receivedFrom)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Invalid borrow request. The book can only be borrowed from " + validReceivedFrom + ".");
            }
    
            // Create new borrow history
            BorrowHistory history = new BorrowHistory(book, borrowerName, receivedFrom, LocalDateTime.now());
            history.setBookDetail(book.getBookDetail());
            historyRepository.save(history);

            // Update book status
            book.setBorrowed(true); // Mark book as borrowed
            book.setBorrowCount(book.getBorrowCount() + 1); // Increment borrow count
            book.setCurrentHolder(borrowerName); // Update currentHolder
            
            return repository.save(book);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PutMapping("/books/{id}/return")
    Storage returnBook(@PathVariable Long id, @RequestBody Map<String, String> returnDetails) {
        String returnedTo = returnDetails.get("returnedTo");

        return repository.findById(id).map(book -> {
            List<BorrowHistory> histories = historyRepository.findByBookId(id);
            if (histories.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No borrow history found for this book.");
            }

            BorrowHistory lastHistory = histories.get(histories.size() - 1);

            // Validate if `returnedTo` is in the list of `receivedFrom`
            boolean isValidReturn = histories.stream()
                .anyMatch(history -> history.getReceivedFrom().equals(returnedTo));

            if (!isValidReturn) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid return. The person returning the book must be in the borrow history.");
            }

            // Set return details
            lastHistory.setReturnDate(LocalDateTime.now());
            lastHistory.setReturned(true);
            historyRepository.save(lastHistory);

            book.setCurrentHolder(returnedTo); // Update current holder
            return repository.save(book);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PutMapping("/books/{bookId}/return/{borrowId}")
    Storage returnBook(@PathVariable Long bookId, @PathVariable Long borrowId, @RequestBody Map<String, String> returnDetails) {
        String returnedTo = returnDetails.get("returnedTo");

        return repository.findById(bookId).map(book -> {
            BorrowHistory historyRecord = historyRepository.findById(borrowId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error: Borrow history record not found for ID " + borrowId));

            // Validate if `returnedTo` is in the borrow history (only receivedFrom should be allowed)
            if (!historyRecord.getReceivedFrom().equals(returnedTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Invalid return. The book must be returned to " + historyRecord.getReceivedFrom() + ".");
            }

            // Mark the specific borrow record as returned
            historyRecord.setReturnDate(LocalDateTime.now());
            historyRecord.setReturned(true);
            historyRepository.save(historyRecord);

            // Update current holder based on latest unreturned borrow history
            List<BorrowHistory> histories = historyRepository.findByBookId(bookId);
            BorrowHistory lastUnreturned = histories.stream()
                .filter(h -> !h.isReturned())
                .reduce((first, second) -> second) // Get the last unreturned record
                .orElse(null);

            if (lastUnreturned != null) {
                book.setCurrentHolder(lastUnreturned.getBorrowerName()); // The last unreturned borrower is the holder
            } else {
                book.setCurrentHolder(book.getOwnerName()); // If all books are returned, it's back to the owner
            }

            // Update `borrowed` flag
            book.setBorrowed(!book.getCurrentHolder().equals(book.getOwnerName()));

            return repository.save(book);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error: Book with ID " + bookId + " not found"));
    }



    @GetMapping("/books/{id}/history")
    List<BorrowHistory> getBorrowHistory(@PathVariable Long id) {
        List<BorrowHistory> histories = historyRepository.findByBookId(id);
        histories.forEach(history -> history.getBookDetail()); // Ensure bookDetail is populated
        return histories;
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/books/{id}")
    void deletebook(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
