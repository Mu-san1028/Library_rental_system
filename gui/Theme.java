package gui;

import java.awt.Color;
import java.awt.Font;

public class Theme {

    // Colors
    public static final Color SIDEBAR_BG      = Color.decode("#1E1E2E");
    public static final Color SIDEBAR_ACTIVE  = Color.decode("#7C6FF0");
    public static final Color CONTENT_BG      = Color.decode("#F5F5FA");
    public static final Color CARD_BG         = Color.decode("#FFFFFF");
    public static final Color ACCENT          = Color.decode("#7C6FF0");
    public static final Color SUCCESS         = Color.decode("#34C77B");
    public static final Color WARNING         = Color.decode("#F0A05A");
    public static final Color DANGER          = Color.decode("#E15A5A");
    public static final Color TEXT_PRIMARY    = Color.decode("#2A2A35");
    public static final Color TEXT_SECONDARY  = Color.decode("#8A8A99");

    // Fonts
    public static final Font FONT_HEADING = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Font FONT_SUBHEADING = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_BODY = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

    private Theme() {
        // prevent instantiation, this class only holds constants
    }
}