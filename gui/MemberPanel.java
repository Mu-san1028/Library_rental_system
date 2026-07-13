package gui;

import gui.components.PlaceholderTextField;
import gui.components.RoundedButton;
import gui.components.StyledTable;
import library.Library;
import model.Book;
import model.Member;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MemberPanel extends JPanel {

    private final Library library;
    private final MemberTableModel tableModel;
    private final StyledTable table;
    private final PlaceholderTextField searchField;

    public MemberPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Members");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        searchField = new PlaceholderTextField("Search by name or member ID...");
        searchField.setFont(Theme.FONT_BODY);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onSearch(); }
            @Override public void removeUpdate(DocumentEvent e) { onSearch(); }
            @Override public void changedUpdate(DocumentEvent e) { onSearch(); }
        });

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new MemberTableModel(library.findMembers(""));
        table = new StyledTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(400);  // Borrowed Books
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addButton = new RoundedButton("Add Member");
        JButton editButton = new RoundedButton("Edit Member");
        JButton deleteButton = new RoundedButton("Delete Member");

        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        deleteButton.addActionListener(e -> onDelete());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onSearch() {
        String keyword = searchField.getText();
        tableModel.setMembers(library.findMembers(keyword));
    }

    private Member getSelectedMember() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return tableModel.getMemberAt(row);
    }

    private void onAdd() {
        String name = JOptionPane.showInputDialog(this, "Member name:");
        if (name == null) return; // cancelled
        try {
            library.addMember(new Member(name.trim()));
            onSearch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEdit() {
        Member member = getSelectedMember();
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Please select a member first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newName = JOptionPane.showInputDialog(this, "Member name:", member.getName());
        if (newName == null) return;
        try {
            member.setName(newName.trim());
            onSearch();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        Member member = getSelectedMember();
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Please select a member first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete \"" + member.getName() + "\"?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                library.removeMember(member);
                onSearch();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot delete", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class MemberTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Name", "Borrowed Books"};
        private List<Member> members;

        MemberTableModel(List<Member> members) {
            this.members = members;
        }

        void setMembers(List<Member> members) {
            this.members = members;
            fireTableDataChanged();
        }

        Member getMemberAt(int row) {
            return members.get(row);
        }

        @Override public int getRowCount() { return members.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Member member = members.get(row);
            switch (col) {
                case 0: return member.getMemberId();
                case 1: return member.getName();
                case 2:
                    List<Book> borrowed = member.getBorrowedBooks();
                    if (borrowed.isEmpty()) return "-";
                    return borrowed.stream().map(Book::getTitle).collect(Collectors.joining(", "));
                default: return null;
            }
        }
    }

    public void refresh() {
        onSearch();
    }
}