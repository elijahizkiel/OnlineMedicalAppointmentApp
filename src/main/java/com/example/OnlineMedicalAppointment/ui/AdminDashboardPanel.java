package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for displaying the admin dashboard.
 */
public class AdminDashboardPanel extends JPanel {

    private final User currentUser;

    /**
     * Constructs the AdminDashboardPanel for the given user.
     * 
     * @param user the current user
     */
    public AdminDashboardPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("Welcome, Admin " + currentUser.getFName() + "! This is the Admin Dashboard Panel."),
                BorderLayout.NORTH);
        // TODO: Add components for statistics and user lists
    }
}
