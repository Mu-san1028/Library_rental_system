import gui.MainFrame;
import library.Library;

public class Main {
    public static void main(String[] args) throws Exception {
        Library library = new Library();
        library.loadBooksFromCsv("data/books.csv");
        library.loadMembersFromCsv("data/members.csv");

        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(library);
            frame.setVisible(true);
        });
    }
}