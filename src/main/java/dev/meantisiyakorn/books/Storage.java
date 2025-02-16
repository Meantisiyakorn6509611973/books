package dev.meantisiyakorn.books;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;


@Entity
public class Storage {
    private @Id
    @GeneratedValue Long id;
    private String bookDetail;
    private String ownerName;
    private String locationName;
    private Boolean borrowed;
    private int borrowCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowHistory> borrowHistory;

    private String currentHolder; 

    Storage() {}

    public Storage(String bookDetail, String ownerName, String locationName, Boolean borrowed, String borrowerName) {
        this.bookDetail = bookDetail;
        this.ownerName = ownerName;
        this.locationName = locationName;
        this.borrowed = borrowed;
        this.borrowCount = 0;
        this.currentHolder = null;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookDetail() {
        return bookDetail;
    }
    public void setBookDetail(String bookDetail) {
        this.bookDetail = bookDetail;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public Boolean getBorrowed() {
        return borrowed;
    }
    public void setBorrowed(Boolean borrowed) {
        this.borrowed = borrowed;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }
    
    public List<BorrowHistory> getBorrowHistory() {
        return borrowHistory;
    }

    public void setBorrowHistory(List<BorrowHistory> borrowHistory) {
        this.borrowHistory = borrowHistory;
    }

    public String getCurrentHolder() {
        return currentHolder;
    }

    public void setCurrentHolder(String currentHolder) {
        this.currentHolder = currentHolder;
        if (currentHolder.equals(this.ownerName)) {
            this.borrowed = false; // Automatically set borrowed to false if book is with the owner
        } else {
            this.borrowed = true;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((bookDetail == null) ? 0 : bookDetail.hashCode());
        result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
        result = prime * result + ((locationName == null) ? 0 : locationName.hashCode());
        result = prime * result + ((borrowed == null) ? 0 : borrowed.hashCode());
       
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Storage other = (Storage) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (bookDetail == null) {
            if (other.bookDetail != null)
                return false;
        } else if (!bookDetail.equals(other.bookDetail))
            return false;
        if (ownerName == null) {
            if (other.ownerName != null)
                return false;
        } else if (!ownerName.equals(other.ownerName))
            return false;
        if (locationName == null) {
            if (other.locationName != null)
                return false;
        } else if (!locationName.equals(other.locationName))
            return false;
        if (borrowed == null) {
            if (other.borrowed != null)
                return false;
        } else if (!borrowed.equals(other.borrowed))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Storage [id=" + id + ", bookDetail=" + bookDetail + ", ownerName=" + ownerName 
                + ", locationName=" + locationName + ", borrowed=" + borrowed + ", borrowCount=" + borrowCount 
                + ", currentHolder=" + currentHolder + "]";
    }
}
