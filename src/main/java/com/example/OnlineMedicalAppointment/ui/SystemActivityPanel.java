package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;

public class SystemActivityPanel extends JPanel {
    public SystemActivityPanel() {
        setLayout(new BorderLayout());
        
        // Title panel
        JLabel titleLabel = StyleConstants.createLabel("Appointment Cancellation Statistics", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Content will be added here
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("Cancellation statistics by user type will appear here"));
        add(contentPanel, BorderLayout.CENTER);
    }
}
