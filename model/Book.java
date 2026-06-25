package model;

public class Book {
    // Basic information about the book
    private String title;
    private String genre;
    private int totalCopies;
    private int loanedCopies;

    // Constructor
    public Book(String title, String genre, int totalCopies) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty.");
        }
        if (totalCopies < 1) {
            throw new IllegalArgumentException("Total copies cannot be less than 1.");
        }
        this.title = title;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.loanedCopies = 0;
    }

    // Getters
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getTotalCopies() { return totalCopies; }
    public int getLoanedCopies() { return loanedCopies; }

    // Setters
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }
    public void setGenre(String genre) {
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty.");
        }
        this.genre = genre;
    }
    public void setTotalCopies(int totalCopies) {
        if (totalCopies < loanedCopies) {
            throw new IllegalArgumentException("Total copies cannot be less than the number of loaned copies.");
        }
        this.totalCopies = totalCopies;
    }
}
