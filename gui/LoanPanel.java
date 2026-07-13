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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LoanPanel extends JPanel {

    private final Library library;
    private final LoanTableModel tableModel;
    private final StyledTable table;

    private final JComboBox<String> memberCombo;
    private final JComboBox<String> bookCombo;
    private final Map<String, Member> memberLookup = new HashMap<>();
    private final Map<String, Book> bookLookup = new HashMap<>();
    private List<String> allMemberLabels = new ArrayList<>();
    private List<String> allBookLabels = new ArrayList<>();

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
        memberCombo.setEditable(true);
        memberCombo.setPreferredSize(new Dimension(250, 28));

        bookCombo = new JComboBox<>();
        bookCombo.setEditable(true);
        bookCombo.setPreferredSize(new Dimension(250, 28));

        setupAutoComplete(memberCombo, () -> allMemberLabels);
        setupAutoComplete(bookCombo, () -> allBookLabels);

        JPanel memberRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberRow.setOpaque(false);
        memberRow.add(new JLabel("Member:"));
        memberRow.add(memberCombo);

        JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookRow.setOpaque(false);
        bookRow.add(new JLabel("Book:"));
        bookRow.add(bookCombo);

        JButton checkOutButton = new RoundedButton("Check Out");
        JButton checkInButton = new RoundedButton("Check In");
        checkOutButton.addActionListener(e -> onCheckOut());
        checkInButton.addActionListener(e -> onCheckIn());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.setOpaque(false);
        buttonRow.add(checkOutButton);
        buttonRow.add(checkInButton);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(memberRow);
        formPanel.add(bookRow);
        formPanel.add(buttonRow);

        add(formPanel, BorderLayout.SOUTH);

        refresh();
    }

    private void setupAutoComplete(JComboBox<String> comboBox, Supplier<List<String>> source) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN
                        || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_ESCAPE) {
                    return;
                }
                String text = editor.getText();
                List<String> matches = new ArrayList<>();
                if (!text.isEmpty()) {
                    String lower = text.toLowerCase();
                    for (String label : source.get()) {
                        if (label.toLowerCase().contains(lower)) matches.add(label);
                    }
                }
                if (matches.isEmpty()) {
                    comboBox.hidePopup();
                } else {
                    comboBox.setModel(new DefaultComboBoxModel<>(matches.toArray(new String[0])));
                    editor.setText(text);
                    editor.setCaretPosition(text.length());
                    comboBox.showPopup();
                }
            }
        });
    }

    public void refresh() {
        memberLookup.clear();
        allMemberLabels = new ArrayList<>();
        for (Member member : library.findMembers("")) {
            String label = labelFor(member);
            memberLookup.put(label, member);
            allMemberLabels.add(label);
        }

        bookLookup.clear();
        allBookLabels = new ArrayList<>();
        for (Book book : library.findBooks("")) {
            String label = labelFor(book);
            bookLookup.put(label, book);
            allBookLabels.add(label);
        }

        memberCombo.setModel(new DefaultComboBoxModel<>(allMemberLabels.toArray(new String[0])));
        bookCombo.setModel(new DefaultComboBoxModel<>(allBookLabels.toArray(new String[0])));
        memberCombo.getEditor().setItem("");
        bookCombo.getEditor().setItem("");

        tableModel.setLoans(library.getAllLoans());
    }

    private String labelFor(Member member) {
        return member.getName() + " (ID " + member.getMemberId() + ")";
    }

    private String labelFor(Book book) {
        int available = book.getTotalCopies() - book.getLoanedCopies();
        return book.getTitle() + " - " + available + "/" + book.getTotalCopies() + " available";
    }

    private Member resolveMember() {
        String text = (String) memberCombo.getEditor().getItem();
        if (text == null || text.isBlank()) return null;
        Member exact = memberLookup.get(text);
        if (exact != null) return exact;
        List<Member> matches = library.findMembers(text);
        return matches.size() == 1 ? matches.get(0) : null;
    }

    private Book resolveBook() {
        String text = (String) bookCombo.getEditor().getItem();
        if (text == null || text.isBlank()) return null;
        Book exact = bookLookup.get(text);
        if (exact != null) return exact;
        List<Book> matches = library.findBooks(text);
        return matches.size() == 1 ? matches.get(0) : null;
    }

    private void onCheckOut() {
        Member member = resolveMember();
        Book book = resolveBook();
        if (member == null || book == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a suggestion, or type a name/title that matches exactly one entry.",
                    "Not found", JOptionPane.WARNING_MESSAGE);
            return;
        }
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