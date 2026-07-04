package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SidebarPanel extends JPanel {

    private final JButton dashboardButton;
    private final JButton booksButton;
    private final JButton membersButton;
    private final JButton loansButton;
    private JButton activeButton;

    public SidebarPanel(Consumer<String> onNavigate) {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR_BG);
        setPreferredSize(new Dimension(200, 0));

        JLabel logo = new JLabel("Library System");
        logo.setFont(Theme.FONT_HEADING);
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        add(logo, BorderLayout.NORTH);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);

        dashboardButton = createNavButton("Dashboard");
        booksButton = createNavButton("Books");
        membersButton = createNavButton("Members");
        loansButton = createNavButton("Loans");

        dashboardButton.addActionListener(e -> select(dashboardButton, "Dashboard", onNavigate));
        booksButton.addActionListener(e -> select(booksButton, "Books", onNavigate));
        membersButton.addActionListener(e -> select(membersButton, "Members", onNavigate));
        loansButton.addActionListener(e -> select(loansButton, "Loans", onNavigate));

        navPanel.add(dashboardButton);
        navPanel.add(booksButton);
        navPanel.add(membersButton);
        navPanel.add(loansButton);

        add(navPanel, BorderLayout.CENTER);

        select(dashboardButton, "Dashboard", onNavigate);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Theme.FONT_BODY);
        button.setForeground(Color.WHITE);
        button.setBackground(Theme.SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void select(JButton button, String panelName, Consumer<String> onNavigate) {
        if (activeButton != null) {
            activeButton.setBackground(Theme.SIDEBAR_BG);
        }
        button.setBackground(Theme.SIDEBAR_ACTIVE);
        activeButton = button;
        onNavigate.accept(panelName);
    }
}