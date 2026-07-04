package gui;

import library.Library;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final DashboardPanel dashboardPanel;
    private final BookPanel bookPanel;
    private final MemberPanel memberPanel;
    private final LoanPanel loanPanel;

    public MainFrame(Library library) {
        super("Library System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel(library);
        bookPanel = new BookPanel(library);
        memberPanel = new MemberPanel(library);
        loanPanel = new LoanPanel(library);

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(bookPanel, "Books");
        contentPanel.add(memberPanel, "Members");
        contentPanel.add(loanPanel, "Loans");

        SidebarPanel sidebar = new SidebarPanel(this::onNavigate);

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void onNavigate(String panelName) {
        cardLayout.show(contentPanel, panelName);
        switch (panelName) {
            case "Dashboard": dashboardPanel.refresh(); break;
            case "Books": bookPanel.refresh(); break;
            case "Members": memberPanel.refresh(); break;
            case "Loans": loanPanel.refresh(); break;
        }
    }
}