package dev.meantisiyakorn.books;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BorrowHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Storage book;

    private String bookDetail;
    private String borrowerName;
    private String receivedFrom;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private boolean isReturned; 

    public BorrowHistory() {}

    public BorrowHistory(Storage book, String borrowerName, String receivedFrom, LocalDateTime borrowDate) {
        this.book = book;
        this.bookDetail = book.getBookDetail();
        this.borrowerName = borrowerName;
        this.receivedFrom = receivedFrom;
        this.borrowDate = borrowDate;
        this.isReturned = false; // Default: Not returned yet
    }

    public Long getId() {
        return id;
    }

    public Storage getBook() {
        return book;
    }

    public void setBook(Storage book) {
        this.book = book;
        this.bookDetail = book.getBookDetail();
    }

    public String getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(String bookDetail) {
        this.bookDetail = bookDetail;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    @Override
    public String toString() {
        return "BorrowHistory [id=" + id + ", bookDetail=" + bookDetail + ", borrowerName=" + borrowerName 
                + ", receivedFrom=" + receivedFrom + ", borrowDate=" + borrowDate + ", returnDate=" + returnDate 
                + ", isReturned=" + isReturned + "]";
    }


}
