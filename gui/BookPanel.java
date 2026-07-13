package gui;

import gui.components.PlaceholderTextField;
import gui.components.RoundedButton;
import gui.components.StyledTable;
import library.Library;
import model.Book;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {

    private final Library library;
    private final BookTableModel tableModel;
    private final StyledTable table;
    private final PlaceholderTextField searchField;

    public BookPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Books");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        searchField = new PlaceholderTextField("Search by title or genre...");
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

        tableModel = new BookTableModel(library.findBooks(""));
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addButton = new RoundedButton("Add Book");
        JButton editButton = new RoundedButton("Edit Book");
        JButton deleteButton = new RoundedButton("Delete Book");

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
        tableModel.setBooks(library.findBooks(keyword));
    }

    private Book getSelectedBook() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return tableModel.getBookAt(row);
    }

    private void onAdd() {
        JTextField titleField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField copiesField = new JTextField();
        Object[] fields = {
                "Title:", titleField,
                "Genre:", genreField,
                "Total copies:", copiesField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int copies = Integer.parseInt(copiesField.getText().trim());
                library.addBook(new Book(titleField.getText().trim(), genreField.getText().trim(), copies));
                onSearch();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit() {
        Book book = getSelectedBook();
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Please select a book first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField titleField = new JTextField(book.getTitle());
        JTextField genreField = new JTextField(book.getGenre());
        Object[] fields = {
                "Title:", titleField,
                "Genre:", genreField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                book.setTitle(titleField.getText().trim());
                book.setGenre(genreField.getText().trim());
                onSearch();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDelete() {
        Book book = getSelectedBook();
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Please select a book first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete \"" + book.getTitle() + "\"?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                library.removeBook(book);
                onSearch();
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot delete", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class BookTableModel extends AbstractTableModel {
        private final String[] columns = {"Title", "Genre", "Total", "Loaned", "Status"};
        private List<Book> books;

        BookTableModel(List<Book> books) {
            this.books = books;
        }

        void setBooks(List<Book> books) {
            this.books = books;
            fireTableDataChanged();
        }

        Book getBookAt(int row) {
            return books.get(row);
        }

        @Override public int getRowCount() { return books.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Book book = books.get(row);
            switch (col) {
                case 0: return book.getTitle();
                case 1: return book.getGenre();
                case 2: return book.getTotalCopies();
                case 3: return book.getLoanedCopies();
                case 4:
                    if (book.getLoanedCopies() >= book.getTotalCopies()) return "All loaned";
                    if (book.getLoanedCopies() > 0) return "Low";
                    return "Available";
                default: return null;
            }
        }
    }

    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = String.valueOf(value);
            if (!isSelected) {
                switch (status) {
                    case "Available": setForeground(Theme.SUCCESS); break;
                    case "Low": setForeground(Theme.WARNING); break;
                    case "All loaned": setForeground(Theme.DANGER); break;
                    default: setForeground(Theme.TEXT_PRIMARY);
                }
            }
            setFont(Theme.FONT_BODY);
            return c;
        }
    }

    public void refresh() {
        onSearch();
    }
}