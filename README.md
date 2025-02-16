# 📚 Book Borrowing Management System

This is a **Spring Boot REST API** for managing book borrowing in a library-like system. The system allows tracking **who currently has a book, borrowing history, returns, and ownership validation**.

## 🚀 Features
✅ Add new books  
✅ Borrow books only from **current holders or the original owner**  
✅ Track borrowing history  
✅ Return books in a **recorded manner**  
✅ Prevent invalid borrow/return attempts  
✅ Display **who currently has the book**  
✅ Track how many times a book has been borrowed  
✅ Manage books (update, delete)  

---

## 🛠️ **Technology Stack**
- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database (for development)**
- **Maven**
- **REST Client for testing**

---

## 📌 **Setup Instructions**
### 1️⃣ Clone the Repository

```
git clone https://github.com/Meantisiyakorn6509611973/books.git
cd books
```

### 2️⃣ Build the Project
```
mvn clean install
```

### 3️⃣ Run the Application
```
mvn spring-boot:run
```

### 4️⃣ Access H2 Database Console
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave blank)

## API Documentation
### 📚 Book Management APIs
| Method	| Endpoint |	Description |
| -----|-----|----|
| GET	| /books	| Get all books |
| GET	| /books/{id}	| Get details of a specific book |
| POST	| /books	| Add a new book |
| PUT	| /books/{id}	| Update book details |
| DELETE	| /books/{id}	| Delete a book |

### 📖 Borrowing & Returning APIs
| Method	| Endpoint	| Description | 
| ----|-----|----|
| POST	/books/{id}/borrow	| Borrow a book |
| PUT	/books/{bookId}/return/{borrowId}	| Return a specific borrow record |
| GET	/books/{id}/history	| Get borrowing history for a book |

## ❌ Common Errors & Fixes
| Error Message |	Cause	| Solution |
| -----|-----|----|
| Book not found	| The book ID doesn't exist	| Check /books first to confirm book ID |
| Invalid borrow | request	Borrower is not receiving the book from the current holder	| Ensure receivedFrom is correct |
| Borrow history | record not found	Trying to return a book with an incorrect borrow record |	Verify borrow ID using /books/{id}/history |
