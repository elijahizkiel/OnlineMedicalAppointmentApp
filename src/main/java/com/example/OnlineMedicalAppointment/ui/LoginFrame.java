package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.example.OnlineMedicalAppointment.AuthService;
import com.example.OnlineMedicalAppointment.database.DatabaseConnector;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Frame for user login.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton signupButton;

    /**
     * Constructs the LoginFrame.
     */
    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame

        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticateUser(username, password)) {
                    // Get user details from database
                    User user = AuthService.login(username, password); 
                    
                    // Redirect to appropriate dashboard based on user type
                    MainAppFrame mainAppFrame = new MainAppFrame(user);
                    mainAppFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            
        });

        signupButton.addActionListener(e -> {
            // Open Signup Frame
            SignupFrame signupFrame = new SignupFrame();
            signupFrame.setVisible(true);
            dispose();
        });
    }

    /**
     * Authenticates the user with the given username and password.
     * @param username the username
     * @param password the password
     * @return true if authentication is successful, false otherwise
     */
    private boolean authenticateUser(String username, String password) {
        // Use AuthService to authenticate user               

        String query = "SELECT * FROM users_table WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnector.getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return false;
        }
    }

}
