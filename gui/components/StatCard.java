package gui.components;

import gui.Theme;
import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel {

    private final JLabel titleLabel;
    private final JLabel valueLabel;

    public StatCard(String title, int initialValue) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        valueLabel = new JLabel(String.valueOf(initialValue));
        valueLabel.setFont(Theme.FONT_HEADING);
        valueLabel.setForeground(Theme.TEXT_PRIMARY);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(int value) {
        valueLabel.setText(String.valueOf(value));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // subtle shadow, offset slightly down-right
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 16, 16);

        // card background
        g2.setColor(Theme.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, 16, 16);

        g2.dispose();
        super.paintComponent(g);
    }
}