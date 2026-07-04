package gui;

import gui.components.RoundedButton;
import gui.components.StyledTable;
import library.Library;
import model.Book;
import model.Loan;
import model.Member;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class LoanPanel extends JPanel {

    private final Library library;
    private final LoanTableModel tableModel;
    private final StyledTable table;
    private final JComboBox<Member> memberCombo;
    private final JComboBox<Book> bookCombo;

    public LoanPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Loans");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        tableModel = new LoanTableModel(library.getAllLoans());
        table = new StyledTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        memberCombo = new JComboBox<>();
        bookCombo = new JComboBox<>();

        JButton checkOutButton = new RoundedButton("Check Out");
        JButton checkInButton = new RoundedButton("Check In");

        checkOutButton.addActionListener(e -> onCheckOut());
        checkInButton.addActionListener(e -> onCheckIn());

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setOpaque(false);
        formPanel.add(new JLabel("Member:"));
        formPanel.add(memberCombo);
        formPanel.add(new JLabel("Book:"));
        formPanel.add(bookCombo);
        formPanel.add(checkOutButton);
        formPanel.add(checkInButton);
        add(formPanel, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        Member selectedMember = (Member) memberCombo.getSelectedItem();
        Book selectedBook = (Book) bookCombo.getSelectedItem();

        memberCombo.removeAllItems();
        for (Member member : library.findMembers("")) {
            memberCombo.addItem(member);
        }
        bookCombo.removeAllItems();
        for (Book book : library.findBooks("")) {
            bookCombo.addItem(book);
        }

        if (selectedMember != null) memberCombo.setSelectedItem(selectedMember);
        if (selectedBook != null) bookCombo.setSelectedItem(selectedBook);

        tableModel.setLoans(library.getAllLoans());
    }

    private void onCheckOut() {
        Member member = (Member) memberCombo.getSelectedItem();
        Book book = (Book) bookCombo.getSelectedItem();
        if (member == null || book == null) return;
        try {
            library.checkOut(member, book);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot check out", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCheckIn() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a loan to check in.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Loan loan = tableModel.getLoanAt(row);
        try {
            library.checkIn(loan.getMember(), loan.getBook());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot check in", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class LoanTableModel extends AbstractTableModel {
        private final String[] columns = {"Member", "Book", "Loan Date"};
        private List<Loan> loans;

        LoanTableModel(List<Loan> loans) {
            this.loans = loans;
        }

        void setLoans(List<Loan> loans) {
            this.loans = loans;
            fireTableDataChanged();
        }

        Loan getLoanAt(int row) {
            return loans.get(row);
        }

        @Override public int getRowCount() { return loans.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Loan loan = loans.get(row);
            switch (col) {
                case 0: return loan.getMember().getName();
                case 1: return loan.getBook().getTitle();
                case 2: return loan.getLoanDate();
                default: return null;
            }
        }
    }
}