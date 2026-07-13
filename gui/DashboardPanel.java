package gui;

import gui.components.StatCard;
import library.Library;
import model.Book;
import model.Loan;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {

    private final Library library;
    private final StatCard booksCard;
    private final StatCard membersCard;
    private final StatCard loansCard;
    private final DefaultListModel<String> activityListModel;
    private final DefaultListModel<String> mostBorrowedListModel;

    public DashboardPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        booksCard = new StatCard("Books", 0);
        membersCard = new StatCard("Members", 0);
        loansCard = new StatCard("Loans", 0);

        cardsPanel.add(booksCard);
        cardsPanel.add(membersCard);
        cardsPanel.add(loansCard);

        body.add(cardsPanel);
        body.add(Box.createVerticalStrut(24));

        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        listsPanel.setOpaque(false);
        listsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        activityListModel = new DefaultListModel<>();
        listsPanel.add(buildListSection("Recent Activity", activityListModel));

        mostBorrowedListModel = new DefaultListModel<>();
        listsPanel.add(buildListSection("Most Borrowed Books", mostBorrowedListModel));

        body.add(listsPanel);

        add(body, BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildListSection(String heading, DefaultListModel<String> model) {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(Theme.FONT_SUBHEADING);
        headingLabel.setForeground(Theme.TEXT_PRIMARY);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        section.add(headingLabel, BorderLayout.NORTH);

        JList<String> list = new JList<>(model);
        list.setFont(Theme.FONT_BODY);
        list.setForeground(Theme.TEXT_PRIMARY);
        list.setBackground(Theme.CARD_BG);
        list.setFixedCellHeight(26);
        list.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        section.add(new JScrollPane(list), BorderLayout.CENTER);
        return section;
    }

    public void refresh() {
        Map<String, Integer> stats = library.getStats();
        booksCard.setValue(stats.get("totalBooks"));
        membersCard.setValue(stats.get("totalMembers"));
        loansCard.setValue(stats.get("activeLoans"));

        refreshActivity();
        refreshMostBorrowedBooks();
    }

    private void refreshActivity() {
        activityListModel.clear();
        List<Loan> recent = library.getAllLoans().stream()
                .sorted(Comparator.comparing(Loan::getLoanDate).reversed())
                .limit(8)
                .collect(Collectors.toList());

        if (recent.isEmpty()) {
            activityListModel.addElement("No active loans.");
            return;
        }
        for (Loan loan : recent) {
            activityListModel.addElement(loan.getMember().getName() + " borrowed \""
                    + loan.getBook().getTitle() + "\" on " + loan.getLoanDate());
        }
    }

    private void refreshMostBorrowedBooks() {
        mostBorrowedListModel.clear();
        List<Book> mostBorrowed = library.findBooks("").stream()
                .filter(book -> book.getLoanedCopies() > 0)
                .sorted(Comparator.comparingInt(Book::getLoanedCopies).reversed())
                .limit(5)
                .collect(Collectors.toList());

        if (mostBorrowed.isEmpty()) {
            mostBorrowedListModel.addElement("No books are currently borrowed.");
            return;
        }
        for (Book book : mostBorrowed) {
            int loaned = book.getLoanedCopies();
            mostBorrowedListModel.addElement(book.getTitle() + " - " + loaned
                    + (loaned == 1 ? " copy out" : " copies out"));
        }
    }
}
