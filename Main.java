import library.Library;
import model.Book;
import model.Member;

public class Main {
    public static void main(String[] args) throws Exception {
        Library library = new Library();
        library.loadBooksFromCsv("data/books.csv");
        library.loadMembersFromCsv("data/members.csv");

        System.out.println(library.getStats());

        Book book = library.findBooks("Java").isEmpty()
                ? library.findBooks("Clean").get(0)
                : library.findBooks("Java").get(0);
        Member member = library.findMembers("Alice").get(0);

        library.checkOut(member, book);
        System.out.println(library.getStats());
        System.out.println(library.getBorrowers(book));

        library.checkIn(member, book);
        System.out.println(library.getStats());

        try {
            library.removeBook(book); // should succeed now (returned)
            System.out.println("removeBook succeeded as expected");
        } catch (IllegalStateException e) {
            System.out.println("Unexpected: " + e.getMessage());
        }
    }
}