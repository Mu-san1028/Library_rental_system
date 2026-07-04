package model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    // Static counter to generate unique member IDs
    private static int idCounter = 1;

    // Basic information about the member
    private String name;
    private final int memberId;
    private final List<Book> borrowedBooks;

    // Constructor
    public Member(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        this.memberId = idCounter++;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters
    public String getName() { return name; }
    public int getMemberId() { return memberId; }
    public List<Book> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }

    // Setters
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }   

    @Override
    public String toString() {
        return String.format("Member{id=%d, name='%s'}", memberId, name);
    }
}
