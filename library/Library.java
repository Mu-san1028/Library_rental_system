package library;
import model.Book;
import model.Loan;
import model.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Library {
    private List<Book> books;
    private List<Member> members;
    private List<Loan> loans;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null.");
        books.add(book);
    }

    public void addMember(Member member) {
        if (member == null) throw new IllegalArgumentException("Member cannot be null.");
        members.add(member);
    }

    // loan and return methods
    public void checkOut(Member member, Book book) {
        if (member == null) throw new IllegalArgumentException("Member cannot be null.");
        if (book == null) throw new IllegalArgumentException("Book cannot be null.");
        book.loanOneCopy();
        loans.add(new Loan(member, book));
        member.addBorrowedBook(book);
    }

    public void checkIn(Member member, Book book) {
        if (member == null) throw new IllegalArgumentException("Member cannot be null.");
        if (book == null) throw new IllegalArgumentException("Book cannot be null.");
        boolean removed = loans.removeIf(loan -> loan.isForMember(member, book));
        if (!removed) {
            throw new IllegalStateException("This book was not loaned to this member.");
        }  
        book.returnOneCopy();
        member.removeBorrowedBook(book);
    }

    public void removeBook(Book book) {
        if (book.getLoanedCopies() > 0) {
            throw new IllegalStateException("Cannot remove a book that is currently loaned out.");
        }
        books.remove(book);
    }

    public void removeMember(Member member) {
        if (!member.getBorrowedBooks().isEmpty()) {
            throw new IllegalStateException("Cannot remove a member with active loans.");
        }
        members.remove(member);
    }

    public List<Book> findBooks(String keyword) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().contains(keyword) || book.getGenre().contains(keyword)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public List<Member> findMembers(String keyword) {
        List<Member> foundMembers = new ArrayList<>();
        for (Member member : members) {
            if (member.getName().contains(keyword) || String.valueOf(member.getMemberId()).contains(keyword)) {
                foundMembers.add(member);
            }
        }
        return foundMembers;
    }

    public List<Member> getBorrowers(Book book) {
        List<Member> borrowers = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBook().equals(book)) {
                borrowers.add(loan.getMember());
            }
        }
        return borrowers;
    }

    public List<Loan> getLoansOf(Member member) {
        List<Loan> memberLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getMember().equals(member)) {
                memberLoans.add(loan);
            }
        }
        return memberLoans;
    }

    public Map<String, Integer> getStats() {
        int totalBooks = 0;
        int loanedBooks = 0;
        for (Book book : books) {
            totalBooks += book.getTotalCopies();
            loanedBooks += book.getLoanedCopies();
        }

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalBooks", totalBooks);
        stats.put("loanedBooks", loanedBooks);
        stats.put("totalMembers", members.size());
        stats.put("activeLoans", loans.size());
        return stats;
    }

    public void loadBooksFromCsv(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                String title = parts[0].trim();
                String genre = parts[1].trim();
                int totalCopies = Integer.parseInt(parts[2].trim());
                addBook(new Book(title, genre, totalCopies));
            }
        }
    }

    public void loadMembersFromCsv(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                addMember(new Member(line.trim()));
            }
        }
    }

}
