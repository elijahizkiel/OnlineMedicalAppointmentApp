package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.example.OnlineMedicalAppointment.model.Admin;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * The main application frame for the Online Medical Appointment system.
 * This frame adapts its content (tabs and menu options) based on the type of the logged-in user (Patient, Doctor, or Admin).
 */
public class MainAppFrame extends JFrame {

    private User currentUser;
    private final JTabbedPane tabbedPane;
    private JButton geminiButton;

    /**
     * Constructs the main application frame for a given user.
     * Initializes the frame properties, sets the current user, and sets up the UI components
     * including tabs and the menu bar based on the user's type.
     *
     * @param user The user object representing the currently logged-in user.
     */
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
            this.currentUser = (Admin) user;
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

        // Add Gemini floating button
        createFloatingButton();
    }

    /**
     * Sets up the tabs in the JTabbedPane based on the current user's type.
     * Different user types (Patient, Doctor, Admin) will see different sets of tabs.
     */
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
                tabbedPane.addTab("Chat-Room", new DoctorChatPanel(currentUser));
                break;
            }
            case "Admin": {
                tabbedPane.addTab("Dashboard", new AdminDashboardPanel(currentUser));
                tabbedPane.addTab("System Activity", new SystemActivityPanel());
                tabbedPane.addTab("User Management", new UserManagementPanel());
                break;
            }
            // Admin might also need a list of doctors/users tab
            default: // Handle unknown user type or show a default view
                tabbedPane.addTab("Welcome", new JPanel()); // Placeholder
        }
    }

    /**
     * Sets up the menu bar for the frame.
     * Includes a user menu displaying the current user's name with options for viewing the profile and logging out.
     */
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

    private void createFloatingButton() {
        geminiButton = new JButton("Gemini");
        geminiButton.setBackground(StyleConstants.SUCCESS_COLOR);
        geminiButton.setForeground(Color.WHITE);
        geminiButton.setFont(StyleConstants.NORMAL_FONT);
        geminiButton.setFocusPainted(false);
        geminiButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Position at bottom right, but further inside
        int buttonSize = 40;
        int margin = 40;
        geminiButton.setBounds(
            getWidth() - buttonSize - margin,
            getHeight() - buttonSize - margin,
            buttonSize,
            buttonSize
        );
        
        geminiButton.addActionListener(e -> {
            // Open Gemini chat dialog
            openGeminiChat();
        });
        
        // Add to layered pane so it floats above content
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(geminiButton, JLayeredPane.POPUP_LAYER);
        
        // Update position on resize
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                geminiButton.setBounds(
                    getWidth() - buttonSize - margin,
                    getHeight() - buttonSize - margin,
                    buttonSize,
                    buttonSize
                );
            }
        });
    }

    private void openGeminiChat() {
        JDialog dialog = new JDialog(this, "I Assistant", false);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        
        GeminiChatPanel chatPanel = new GeminiChatPanel(currentUser);
        dialog.add(chatPanel);
        
        dialog.setVisible(true);
    }
}