package com.example.OnlineMedicalAppointment.ui;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Patient;  
// import com.example.OnlineMedicalAppointment.model.Admin;    

import javax.swing.*;
import java.awt.*;

public class MainAppFrame extends JFrame {

    private User currentUser;
    private final JTabbedPane tabbedPane;

    public MainAppFrame(User user) {
        String userType = user.getUserType();
        switch (userType) {
            case "Doctor":
            this.currentUser = (Doctor) user;
            break;
            case "Patient":
            this.currentUser = (Patient) user;
            break;
            case "Admin":
            // this.currentUser = (Admin) user;
            break;
            default:
            // Handle unknown user type, maybe throw an exception or log a warning
            System.err.println("Unknown user type: " + userType);
            this.currentUser = user; // Assign the base User object
            break;
        }
        System.out.println("Current User: " + user.getFName() + " " + user.getLName() + ", Type: " + user.getUserType());
        setTitle("Online Medical Appointment - " + user.getUserType());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame

        tabbedPane = new JTabbedPane();

        // Add tabs based on user type
        setupTabs();

        add(tabbedPane, BorderLayout.CENTER);

        // Setup Menu Bar for Profile and Logout
        setupMenuBar();
    }

    private void setupTabs() {
        String userType = currentUser.getUserType();
        switch (userType) {
            case "Patient": {
                tabbedPane.addTab("Home", new PatientHomePanel(currentUser));
                tabbedPane.addTab("Booking", new PatientBookingPanel(currentUser));
                tabbedPane.addTab("Chat-Room", new PatientChatPanel(currentUser));
                break;
            }
            case "Doctor": {
                tabbedPane.addTab("Home", new DoctorHomePanel(currentUser));
                tabbedPane.addTab("Schedule", new DoctorSchedulePanel(currentUser));
                break;
            }
            case "Admin":
                tabbedPane.addTab("Dashboard", new AdminDashboardPanel(currentUser));
                break;
            // Admin might also need a list of doctors/users tab
            default: // Handle unknown user type or show a default view
                tabbedPane.addTab("Welcome", new JPanel()); // Placeholder
        }
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu(currentUser.getFName() + " " + currentUser.getLName()); // Human Avatar/User Name

        JMenuItem profileMenuItem = new JMenuItem("Profile");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");

        profileMenuItem.addActionListener(e -> {
                // Open Profile Panel/Dialog
                ProfilePanel profilePanel = new ProfilePanel(currentUser);
                // You might want to show this in a dialog or a dedicated tab
                 JOptionPane.showMessageDialog(MainAppFrame.this, profilePanel, "User Profile", JOptionPane.PLAIN_MESSAGE);
        });

        logoutMenuItem.addActionListener(e -> {
                // Close current frame and open Login Frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose(); // Close the main app frame
        });

        userMenu.add(profileMenuItem);
        userMenu.add(logoutMenuItem);
        menuBar.add(Box.createHorizontalGlue()); // Push menu to the right
        menuBar.add(userMenu);

        setJMenuBar(menuBar);
    }
}
