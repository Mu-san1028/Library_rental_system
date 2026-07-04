package gui.components;

import gui.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

public class StyledTable extends JTable {

    public StyledTable(TableModel model) {
        super(model);

        setRowHeight(32);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFont(Theme.FONT_BODY);
        setForeground(Theme.TEXT_PRIMARY);
        setSelectionBackground(Theme.ACCENT);
        setSelectionForeground(Color.WHITE);
        setFillsViewportHeight(true);

        JTableHeader header = getTableHeader();
        header.setFont(Theme.FONT_SUBHEADING);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setBackground(Theme.CONTENT_BG);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        setDefaultRenderer(Object.class, new StripedRenderer());
    }

    private class StripedRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Theme.CARD_BG : Theme.CONTENT_BG);
            }
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            return c;
        }
    }
}