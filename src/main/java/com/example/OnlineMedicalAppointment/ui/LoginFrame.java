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
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.AuthService;
import com.example.OnlineMedicalAppointment.database.DatabaseConnector;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Frame for user login functionality.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton signupButton;

    /**
     * Constructs the login frame.
     */
    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame
        getContentPane().setBackground(StyleConstants.LIGHT_BG);

        // Title
        JLabel titleLabel = StyleConstants.createLabel("Medical Appointment System", StyleConstants.TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        titleLabel.setForeground(StyleConstants.PRIMARY_COLOR);
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        inputPanel.setBackground(StyleConstants.LIGHT_BG);

        JLabel usernameLabel = StyleConstants.createLabel("Username:", StyleConstants.NORMAL_FONT);
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameField = new JTextField();
        usernameField.setBorder(StyleConstants.INPUT_BORDER);
        usernameField.setBackground(StyleConstants.WHITE);
        usernameField.setFont(StyleConstants.NORMAL_FONT);
        
        JLabel passwordLabel = StyleConstants.createLabel("Password:", StyleConstants.NORMAL_FONT);
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordField = new JPasswordField();
        passwordField.setBorder(StyleConstants.INPUT_BORDER);
        passwordField.setBackground(StyleConstants.WHITE);
        passwordField.setFont(StyleConstants.NORMAL_FONT);

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(StyleConstants.LIGHT_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new java.awt.Dimension(100, 40));
        loginButton.setBackground(StyleConstants.PRIMARY_COLOR.brighter());
        loginButton.setForeground(StyleConstants.TEXT_COLOR);
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        loginButton.setFocusPainted(false);
        
        signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new java.awt.Dimension(100, 40));
        signupButton.setBackground(StyleConstants.SECONDARY_COLOR.brighter());
        signupButton.setForeground(StyleConstants.TEXT_COLOR);
        signupButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        signupButton.setFocusPainted(false);

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
     * 
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
