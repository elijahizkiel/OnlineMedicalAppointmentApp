package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;

public class SystemActivityPanel extends JPanel {
    public SystemActivityPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title panel
        JLabel titleLabel = StyleConstants.createLabel("System Activity Monitoring", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.add(createStatCard("Patient Cancellations", "25%"));
        statsPanel.add(createStatCard("Doctor Cancellations", "15%"));
        add(statsPanel, BorderLayout.CENTER);
        
        // Detailed view panel
        JTextArea detailsArea = new JTextArea();
        detailsArea.setText("Detailed cancellation statistics will appear here...");
        detailsArea.setBorder(StyleConstants.createTitledBorder("Detailed View"));
        add(new JScrollPane(detailsArea), BorderLayout.SOUTH);
    }
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel(title);
        JLabel valueLabel = new JLabel(value);
        
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}
