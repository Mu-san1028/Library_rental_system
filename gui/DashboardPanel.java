package gui;

import gui.components.StatCard;
import library.Library;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardPanel extends JPanel {

    private final Library library;
    private final StatCard booksCard;
    private final StatCard membersCard;
    private final StatCard loansCard;

    public DashboardPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        booksCard = new StatCard("Books", 0);
        membersCard = new StatCard("Members", 0);
        loansCard = new StatCard("Loans", 0);

        cardsPanel.add(booksCard);
        cardsPanel.add(membersCard);
        cardsPanel.add(loansCard);

        add(cardsPanel, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        Map<String, Integer> stats = library.getStats();
        booksCard.setValue(stats.get("totalBooks"));
        membersCard.setValue(stats.get("totalMembers"));
        loansCard.setValue(stats.get("activeLoans"));
    }
}