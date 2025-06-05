package com.example.OnlineMedicalAppointment.ui;

// import com.example.OnlineMedicalAppointment.database.DatabaseConnector;

import javax.swing.*;

import java.awt.*;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Patient;

public class SignupFrame extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> userTypeComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField specialtyField;
    private JTextField phoneNumberField;
    private JButton signupButton;
    private JButton backToLoginButton;

    public SignupFrame() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(StyleConstants.LIGHT_BG);

        // Title
        JLabel titleLabel = StyleConstants.createLabel("Create Account", StyleConstants.TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        inputPanel.setBackground(StyleConstants.LIGHT_BG);

        JLabel firstNameLabel = StyleConstants.createLabel("First Name:", StyleConstants.NORMAL_FONT);
        firstNameField = new JTextField();
        firstNameField.setBorder(StyleConstants.INPUT_BORDER);
        firstNameField.setBackground(StyleConstants.WHITE);

        JLabel lastNameLabel = StyleConstants.createLabel("Last Name:", StyleConstants.NORMAL_FONT);
        lastNameField = new JTextField();
        lastNameField.setBorder(StyleConstants.INPUT_BORDER);
        lastNameField.setBackground(StyleConstants.WHITE);

        JLabel userTypeLabel = StyleConstants.createLabel("User Type:", StyleConstants.NORMAL_FONT);
        userTypeComboBox = new JComboBox<>(new String[]{"Patient", "Doctor", "Admin"});
        userTypeComboBox.setBorder(StyleConstants.INPUT_BORDER);
        userTypeComboBox.setBackground(StyleConstants.WHITE);

        JLabel usernameLabel = StyleConstants.createLabel("Username:", StyleConstants.NORMAL_FONT);
        usernameField = new JTextField();
        usernameField.setBorder(StyleConstants.INPUT_BORDER);
        usernameField.setBackground(StyleConstants.WHITE);

        JLabel passwordLabel = StyleConstants.createLabel("Password:", StyleConstants.NORMAL_FONT);
        passwordField = new JPasswordField();
        passwordField.setBorder(StyleConstants.INPUT_BORDER);
        passwordField.setBackground(StyleConstants.WHITE);

        JLabel confirmPasswordLabel = StyleConstants.createLabel("Confirm Password:", StyleConstants.NORMAL_FONT);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBorder(StyleConstants.INPUT_BORDER);
        confirmPasswordField.setBackground(StyleConstants.WHITE);

        JLabel specialtyLabel = StyleConstants.createLabel("Specialty (for Doctors):", StyleConstants.NORMAL_FONT);
        specialtyField = new JTextField();
        specialtyField.setBorder(StyleConstants.INPUT_BORDER);
        specialtyField.setBackground(StyleConstants.WHITE);

        JLabel phoneNumberLabel = StyleConstants.createLabel("Phone Number:", StyleConstants.NORMAL_FONT);
        phoneNumberField = new JTextField();
        phoneNumberField.setBorder(StyleConstants.INPUT_BORDER);
        phoneNumberField.setBackground(StyleConstants.WHITE);

        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameField);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameField);
        inputPanel.add(userTypeLabel);
        inputPanel.add(userTypeComboBox);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);
        inputPanel.add(specialtyLabel);
        inputPanel.add(specialtyField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(StyleConstants.LIGHT_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        signupButton = new JButton("Sign Up");
        backToLoginButton = new JButton("Back to Login");

        buttonPanel.add(signupButton);
        buttonPanel.add(backToLoginButton);

        // Add panels to frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        signupButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String userType = (String) userTypeComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String specialty = specialtyField.getText();
            String phoneNumber = phoneNumberField.getText();

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(SignupFrame.this, "Passwords do not match.", "Signup Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (createUser(firstName, lastName, username, password, userType, specialty, phoneNumber)) {
                JOptionPane.showMessageDialog(SignupFrame.this, "Signup successful!");
                // Redirect to Login Frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(SignupFrame.this, "Signup failed. Please try again.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backToLoginButton.addActionListener(e -> {
            // Open Login Frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose(); // Close the signup frame
        });
    }

    private boolean createUser(String firstName, String lastName, String username, String password, String userType, String specialty, String phoneNumber) {
        User user;
        switch (userType) {
            case "Patient":
                user = new Patient(firstName, lastName, username, password, phoneNumber);
                break;
            case "Doctor":
                user = new Doctor(firstName, lastName, username, password, userType, specialty, phoneNumber);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid user type selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
        }
        return DatabaseAccessor.addUser(user);
    }
}