package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;

public class UserManagementPanel extends JPanel {
    public UserManagementPanel() {
        setLayout(new BorderLayout());
        
        // Title panel
        JLabel titleLabel = StyleConstants.createLabel("User Management Dashboard", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Content will be added here
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("User management interface will appear here"));
        add(contentPanel, BorderLayout.CENTER);
    }
}
