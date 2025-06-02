package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;

public class AdminDashboardPanel extends JPanel {

    private User currentUser;

    public AdminDashboardPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("Welcome, Admin " + currentUser.getFName() + "! This is the Admin Dashboard Panel."), BorderLayout.NORTH);
        // TODO: Add components for statistics and user lists
    }
}
