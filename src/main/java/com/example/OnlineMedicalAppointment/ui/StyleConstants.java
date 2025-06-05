package com.example.OnlineMedicalAppointment.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class StyleConstants {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color LIGHT_BG = new Color(248, 249, 250);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(33, 37, 41);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // Borders
    public static final Border INPUT_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );
    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );
    
    public static Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );
    }
    
    public static Border createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(SUBTITLE_FONT);
        border.setTitleColor(PRIMARY_COLOR);
        return border;
    }
    
    // Button styling
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    
    // Text field styling
    public static void styleTextField(JTextField field) {
        field.setFont(NORMAL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(WHITE);
    }
    
    // Label styling
    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    // Panel styling
    public static JPanel createStyledPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
    
    // Create a styled panel with default FlowLayout
    public static JPanel createStyledPanel() {
        return createStyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    }
    
    // Create a section panel with title
    public static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(15, 20, 20, 20)
        ));
        
        JLabel titleLabel = createLabel(title, SUBTITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel);
        
        return panel;
    }
}
