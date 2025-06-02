package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ProfileView extends JPanel {

    private User loggedInUser;
    private PatientHomeView patientHomeView; // To update user details if changed

    // Profile fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JButton saveProfileButton;

    // Password change fields
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmNewPasswordField;
    private JButton changePasswordButton;

    private AuthService authService;

    public ProfileView(User user, PatientHomeView homeView) {
        this.loggedInUser = user;
        this.patientHomeView = homeView;
        this.authService = new AuthService();
        initComponents();
        loadUserProfile();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile Editing Panel
        JPanel profileDetailsPanel = new JPanel(new GridBagLayout());
        profileDetailsPanel.setBorder(BorderFactory.createTitledBorder("Edit Profile"));
        GridBagConstraints gbcProfile = new GridBagConstraints();
        gbcProfile.insets = new Insets(5, 5, 5, 5);
        gbcProfile.fill = GridBagConstraints.HORIZONTAL;

        gbcProfile.gridx = 0; gbcProfile.gridy = 0;
        profileDetailsPanel.add(new JLabel("First Name:"), gbcProfile);
        gbcProfile.gridx = 1; gbcProfile.gridy = 0;
        firstNameField = new JTextField(20);
        profileDetailsPanel.add(firstNameField, gbcProfile);

        gbcProfile.gridx = 0; gbcProfile.gridy = 1;
        profileDetailsPanel.add(new JLabel("Last Name:"), gbcProfile);
        gbcProfile.gridx = 1; gbcProfile.gridy = 1;
        lastNameField = new JTextField(20);
        profileDetailsPanel.add(lastNameField, gbcProfile);

        gbcProfile.gridx = 0; gbcProfile.gridy = 2;
        profileDetailsPanel.add(new JLabel("Phone Number:"), gbcProfile);
        gbcProfile.gridx = 1; gbcProfile.gridy = 2;
        phoneNumberField = new JTextField(20);
        profileDetailsPanel.add(phoneNumberField, gbcProfile);
        
        gbcProfile.gridx = 0; gbcProfile.gridy = 3; gbcProfile.gridwidth = 2; gbcProfile.anchor = GridBagConstraints.CENTER;
        saveProfileButton = new JButton("Save Profile Changes");
        profileDetailsPanel.add(saveProfileButton, gbcProfile);

        // Password Change Panel
        JPanel passwordChangePanel = new JPanel(new GridBagLayout());
        passwordChangePanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.insets = new Insets(5, 5, 5, 5);
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;

        gbcPassword.gridx = 0; gbcPassword.gridy = 0;
        passwordChangePanel.add(new JLabel("Current Password:"), gbcPassword);
        gbcPassword.gridx = 1; gbcPassword.gridy = 0;
        currentPasswordField = new JPasswordField(20);
        passwordChangePanel.add(currentPasswordField, gbcPassword);

        gbcPassword.gridx = 0; gbcPassword.gridy = 1;
        passwordChangePanel.add(new JLabel("New Password:"), gbcPassword);
        gbcPassword.gridx = 1; gbcPassword.gridy = 1;
        newPasswordField = new JPasswordField(20);
        passwordChangePanel.add(newPasswordField, gbcPassword);

        gbcPassword.gridx = 0; gbcPassword.gridy = 2;
        passwordChangePanel.add(new JLabel("Confirm New Password:"), gbcPassword);
        gbcPassword.gridx = 1; gbcPassword.gridy = 2;
        confirmNewPasswordField = new JPasswordField(20);
        passwordChangePanel.add(confirmNewPasswordField, gbcPassword);

        gbcPassword.gridx = 0; gbcPassword.gridy = 3; gbcPassword.gridwidth = 2; gbcPassword.anchor = GridBagConstraints.CENTER;
        changePasswordButton = new JButton("Change Password");
        passwordChangePanel.add(changePasswordButton, gbcPassword);

        // Add panels to main layout
        add(profileDetailsPanel, BorderLayout.NORTH);
        add(passwordChangePanel, BorderLayout.CENTER);

        // Action Listeners
        saveProfileButton.addActionListener(e -> saveProfileChangesAction());
        changePasswordButton.addActionListener(e -> changePasswordAction());
    }

    private void loadUserProfile() {
        firstNameField.setText(loggedInUser.firstName);
        lastNameField.setText(loggedInUser.lastName);
        // Need to fetch full user details if phone number isn't in the loggedInUser object from login
        // For now, assume it might be null or we'd fetch it.
        // Let's assume loggedInUser from Auth Service might not have phone, so we fetch latest.
        User freshUserDetails = DatabaseManager.getUserById(loggedInUser.userID);
        if (freshUserDetails != null) {
            this.loggedInUser = freshUserDetails; // Update local reference
            firstNameField.setText(freshUserDetails.firstName);
            lastNameField.setText(freshUserDetails.lastName);
            phoneNumberField.setText(freshUserDetails.phoneNumber != null ? freshUserDetails.phoneNumber : "");
        }
    }

    private void saveProfileChangesAction() {
        String newFirstName = firstNameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String newPhoneNumber = phoneNumberField.getText().trim();

        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name and last name cannot be empty.", "Profile Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = DatabaseManager.updateUserProfile(loggedInUser.userID, newFirstName, newLastName, newPhoneNumber);
        if (success) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Profile Update", JOptionPane.INFORMATION_MESSAGE);
            // Update the user object in this view and in PatientHomeView
            loggedInUser.firstName = newFirstName;
            loggedInUser.lastName = newLastName;
            loggedInUser.phoneNumber = newPhoneNumber;
            if (patientHomeView != null) {
                patientHomeView.updateLoggedInUser(loggedInUser); // Need to add this method to PatientHomeView
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.", "Profile Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePasswordAction() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmNewPasswordField.getPassword());

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All password fields are required.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPassword.length() < 6) { // Example: Basic password length validation
            JOptionPane.showMessageDialog(this, "New password must be at least 6 characters long.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authService.changePassword(loggedInUser.userID, currentPassword, newPassword);
        if (success) {
            JOptionPane.showMessageDialog(this, "Password changed successfully.", "Password Change", JOptionPane.INFORMATION_MESSAGE);
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmNewPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change password. Check current password or try again.", "Password Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
