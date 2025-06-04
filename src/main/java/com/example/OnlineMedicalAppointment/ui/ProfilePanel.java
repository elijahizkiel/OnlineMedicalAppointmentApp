package com.example.OnlineMedicalAppointment.ui;

import com.example.OnlineMedicalAppointment.model.*;

import javax.swing.*;
import java.awt.*;
public class ProfilePanel extends JPanel {

    private final User currentUser;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JButton editButton;
    private final JButton changePasswordButton;

    public ProfilePanel(User user) {
        this.currentUser = user;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(currentUser.getFName());
        firstNameField.setEditable(false); // Initially not editable
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(currentUser.getLName());
        lastNameField.setEditable(false); // Initially not editable
        add(lastNameField);

        add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(currentUser.getPhoneNumber());
        phoneNumberField.setEditable(false); // Initially not editable
        add(phoneNumberField);

        editButton = new JButton("Edit Profile");
        changePasswordButton = new JButton("Change Password");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editButton);
        buttonPanel.add(changePasswordButton);

        // Add button panel spanning two columns
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc); // Note: GridLayout doesn't use GBC, need to adjust layout or add panel directly

        // Re-layout for buttons if using GridLayout
        removeAll(); // Remove GridLayout components
        setLayout(new GridBagLayout()); // Use GridBagLayout for better control

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5,5,5,5);
        add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1; add(phoneNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; // Center buttons
        add(buttonPanel, gbc);


        editButton.addActionListener(e -> {
            // Toggle editability and button text
            boolean editable = firstNameField.isEditable();
            firstNameField.setEditable(!editable);
            lastNameField.setEditable(!editable);
            phoneNumberField.setEditable(!editable);
            editButton.setText(editable ? "Edit Profile" : "Save Profile");

            if (editable) {
                // Save changes to database
                saveProfileChanges();
            }

        });

        changePasswordButton.addActionListener(e -> {
            // Open Change Password Dialog
            showChangePasswordDialog();
        });

        editButton.addActionListener(e -> {
                // Toggle editability and button text
                boolean editable = firstNameField.isEditable();
                firstNameField.setEditable(!editable);
                lastNameField.setEditable(!editable);
                phoneNumberField.setEditable(!editable);
                editButton.setText(editable ? "Edit Profile" : "Save Profile");

                if (editable) {
                    // Save changes to database
                    saveProfileChanges();
                }
            
        });

        changePasswordButton.addActionListener(e -> {
                // Open Change Password Dialog
                showChangePasswordDialog();
        });
    }

    private void saveProfileChanges() {
        currentUser.setFName(firstNameField.getText());
        currentUser.setLName(lastNameField.getText());
        currentUser.setPhoneNumber(phoneNumberField.getText());
        
        currentUser.updateUser();
        JOptionPane.showMessageDialog(this, "Profile changes saved (Database update not yet implemented).");
    }

    private void showChangePasswordDialog() {
        // Create the dialog panel
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmNewPasswordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Old Password:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmNewPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmNewPassword = new String(confirmNewPasswordField.getPassword());
            if(currentUser.getPassword().equals(oldPassword)){
            // Update the password in the user object
            if (newPassword.equals(confirmNewPassword)) {
                currentUser.setPassword(newPassword); 
                if (currentUser.getUserType().equals("Patient")){  
                    Patient user = (Patient) currentUser;
                    user.updateUser();
                }else if (currentUser.getUserType().equals("Doctor")){
                        Doctor user = (Doctor) currentUser;
                        user.updateUser();
                }else if (currentUser.getUserType().equals("Admin")){
                        Admin user = (Admin) currentUser;
                        user.updateUser();
                }

                    JOptionPane.showMessageDialog(this, "Password changed succesfully.");
            } else {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        }
    }
}
