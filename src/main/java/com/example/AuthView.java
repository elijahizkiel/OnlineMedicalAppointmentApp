package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AuthView extends JFrame {

    private AuthService authService;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel signupPanel;

    // Login components
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JButton showSignupButton;

    // Signup components
    private JTextField signupFirstNameField;
    private JTextField signupLastNameField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JPasswordField signupConfirmPasswordField;
    private JComboBox<String> signupUserTypeComboBox;
    private JTextField signupSpecialtyField;
    private JLabel specialtyLabel;
    private JButton signupButton;
    private JButton showLoginButton;

    public AuthView() {
        authService = new AuthService();
        setTitle("User Authentication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400); // Adjusted size
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createSignupPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(signupPanel, "signup");

        add(mainPanel);
        cardLayout.show(mainPanel, "login"); // Show login panel by default
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        loginUsernameField = new JTextField(20);
        loginPanel.add(loginUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        loginPasswordField = new JPasswordField(20);
        loginPanel.add(loginPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        showSignupButton = new JButton("Don't have an account? Sign Up");
        loginPanel.add(showSignupButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        showSignupButton.addActionListener(e -> cardLayout.show(mainPanel, "signup"));
    }

    private void createSignupPanel() {
        signupPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        signupPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        signupFirstNameField = new JTextField(20);
        signupPanel.add(signupFirstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        signupPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        signupLastNameField = new JTextField(20);
        signupPanel.add(signupLastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        signupPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        signupUsernameField = new JTextField(20);
        signupPanel.add(signupUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        signupPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        signupPasswordField = new JPasswordField(20);
        signupPanel.add(signupPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        signupPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        signupConfirmPasswordField = new JPasswordField(20);
        signupPanel.add(signupConfirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        signupPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        signupUserTypeComboBox = new JComboBox<>(new String[]{"Patient", "Doctor"});
        signupPanel.add(signupUserTypeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        specialtyLabel = new JLabel("Specialty:");
        signupPanel.add(specialtyLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        signupSpecialtyField = new JTextField(20);
        signupPanel.add(signupSpecialtyField, gbc);
        specialtyLabel.setVisible(false); // Initially hidden
        signupSpecialtyField.setVisible(false); // Initially hidden

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        signupButton = new JButton("Sign Up");
        signupPanel.add(signupButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        showLoginButton = new JButton("Already have an account? Login");
        signupPanel.add(showLoginButton, gbc);

        signupUserTypeComboBox.addActionListener(e -> {
            boolean isDoctor = "Doctor".equals(signupUserTypeComboBox.getSelectedItem());
            specialtyLabel.setVisible(isDoctor);
            signupSpecialtyField.setVisible(isDoctor);
        });

        signupButton.addActionListener(e -> handleSignup());
        showLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
    }

    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authService.loginUser(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Logged in user details: " + user.toString());
            if ("Patient".equals(user.userType)) {
                // Open PatientHomeView
                SwingUtilities.invokeLater(() -> {
                    new PatientHomeView(user).setVisible(true);
                    this.dispose(); // Close AuthView
                });
            } else if ("Doctor".equals(user.userType)) {
                SwingUtilities.invokeLater(() -> {
                    new DoctorHomeView(user).setVisible(true);
                    this.dispose(); // Close AuthView
                });
            } else if ("Admin".equals(user.userType)) {
                 // Placeholder for Admin view
                JOptionPane.showMessageDialog(this, "Admin login successful. Admin dashboard not yet implemented.", "Admin Login", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup() {
        String firstName = signupFirstNameField.getText();
        String lastName = signupLastNameField.getText();
        String username = signupUsernameField.getText();
        String password = new String(signupPasswordField.getPassword());
        String confirmPassword = new String(signupConfirmPasswordField.getPassword());
        String userType = (String) signupUserTypeComboBox.getSelectedItem();
        String specialty = signupSpecialtyField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ("Doctor".equals(userType) && (specialty == null || specialty.trim().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Specialty is required for Doctor user type.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        boolean success = authService.signUpUser(firstName, lastName, username, password, userType, specialty);
        if (success) {
            JOptionPane.showMessageDialog(this, "Signup successful! You can now login.", "Signup Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "login"); // Switch to login panel
        } else {
            // More specific error (e.g., username exists) might be handled in AuthService or DatabaseManager and propagated
            JOptionPane.showMessageDialog(this, "Signup failed. Username might already exist or another error occurred.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
