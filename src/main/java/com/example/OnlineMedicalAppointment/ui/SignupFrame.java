package com.example.OnlineMedicalAppointment.ui;

// import com.example.OnlineMedicalAppointment.database.DatabaseConnector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;

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

        // Subtitle
        JLabel subtitleLabel = StyleConstants.createLabel("Please fill in your details to create an account.", StyleConstants.SUBTITLE_FONT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(subtitleLabel, BorderLayout.BEFORE_FIRST_LINE);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(StyleConstants.createRoundedBorder());
        inputPanel.setBackground(StyleConstants.LIGHT_BG);

        JLabel firstNameLabel = StyleConstants.createLabel("First Name:", StyleConstants.NORMAL_FONT);
        firstNameField = new JTextField();
        StyleConstants.styleTextField(firstNameField);
        firstNameField.setToolTipText("Enter your first name");

        JLabel lastNameLabel = StyleConstants.createLabel("Last Name:", StyleConstants.NORMAL_FONT);
        lastNameField = new JTextField();
        StyleConstants.styleTextField(lastNameField);
        lastNameField.setToolTipText("Enter your last name");

        JLabel userTypeLabel = StyleConstants.createLabel("User Type:", StyleConstants.NORMAL_FONT);
        userTypeComboBox = new JComboBox<>(new String[]{"Patient", "Doctor", "Admin"});
        userTypeComboBox.setFont(StyleConstants.NORMAL_FONT);
        userTypeComboBox.setBorder(StyleConstants.INPUT_BORDER);
        userTypeComboBox.setBackground(StyleConstants.WHITE);
        userTypeComboBox.setToolTipText("Select your user type");

        JLabel usernameLabel = StyleConstants.createLabel("Username:", StyleConstants.NORMAL_FONT);
        usernameField = new JTextField();
        StyleConstants.styleTextField(usernameField);
        usernameField.setToolTipText("Choose a unique username");

        JLabel passwordLabel = StyleConstants.createLabel("Password:", StyleConstants.NORMAL_FONT);
        passwordField = new JPasswordField();
        StyleConstants.styleTextField(passwordField);
        passwordField.setToolTipText("Enter your password");

        JLabel confirmPasswordLabel = StyleConstants.createLabel("Confirm Password:", StyleConstants.NORMAL_FONT);
        confirmPasswordField = new JPasswordField();
        StyleConstants.styleTextField(confirmPasswordField);
        confirmPasswordField.setToolTipText("Re-enter your password");

        JLabel specialtyLabel = StyleConstants.createLabel("Specialty (for Doctors):", StyleConstants.NORMAL_FONT);
        specialtyField = new JTextField();
        StyleConstants.styleTextField(specialtyField);
        specialtyField.setToolTipText("Enter your specialty if you are a doctor");

        JLabel phoneNumberLabel = StyleConstants.createLabel("Phone Number:", StyleConstants.NORMAL_FONT);
        phoneNumberField = new JTextField();
        StyleConstants.styleTextField(phoneNumberField);
        phoneNumberField.setToolTipText("Enter your phone number");

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

        // Hide specialty field unless Doctor is selected
        specialtyLabel.setVisible(false);
        specialtyField.setVisible(false);
        userTypeComboBox.addActionListener(e -> {
            String selected = (String) userTypeComboBox.getSelectedItem();
            boolean isDoctor = "Doctor".equals(selected);
            specialtyLabel.setVisible(isDoctor);
            specialtyField.setVisible(isDoctor);
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(StyleConstants.LIGHT_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        signupButton = StyleConstants.createPrimaryButton("Sign Up");
        backToLoginButton = StyleConstants.createPrimaryButton("Back to Login");
        backToLoginButton.setBackground(StyleConstants.SECONDARY_COLOR);
        backToLoginButton.setForeground(StyleConstants.WHITE);

        buttonPanel.add(signupButton);
        buttonPanel.add(backToLoginButton);

        // Add panels to frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add focus highlight to fields
        JTextField[] fields = {firstNameField, lastNameField, usernameField, passwordField, confirmPasswordField, specialtyField, phoneNumberField};
        for (JTextField field : fields) {
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    field.setBackground(new Color(232, 240, 254));
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    field.setBackground(StyleConstants.WHITE);
                }
            });
        }

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