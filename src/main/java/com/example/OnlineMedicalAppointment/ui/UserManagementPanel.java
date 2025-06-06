package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.User;
import java.util.List;
import javax.swing.table.DefaultTableModel;

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
        String[] columnNames = {"ID", "Name", "Email", "User Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        List<User> users = DatabaseAccessor.getAllUsers();
        for (User user : users) {
            String name = user.getFName() + " " + user.getLName();
            tableModel.addRow(new Object[]{user.getUserID(), name, user.getUsername(), user.getUserType()});
        }
        JTable userTable = new JTable(tableModel);
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
