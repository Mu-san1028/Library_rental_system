package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Corrections;

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
}
