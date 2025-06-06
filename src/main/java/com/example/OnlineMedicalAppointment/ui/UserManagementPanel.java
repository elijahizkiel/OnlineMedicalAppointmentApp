package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;

public class UserManagementPanel extends JPanel {
    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title panel
        JLabel titleLabel = StyleConstants.createLabel("User Management Dashboard", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Table for user data
        String[] columnNames = {"ID", "Name", "Email", "User Type", "Actions"};
        Object[][] data = {}; // Will be populated from database
        JTable userTable = new JTable(data, columnNames);
        userTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(StyleConstants.createTitledBorder("All Users"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton =new JButton("Refresh");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
