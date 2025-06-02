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

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeComboBox = new JComboBox<>(new String[]{"Patient", "Doctor", "Admin"});
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();
        JLabel specialtyLabel = new JLabel("Specialty (for Doctors):");
        specialtyField = new JTextField();
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberField = new JTextField();

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

        backToLoginButton.addActionListener( e-> {
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