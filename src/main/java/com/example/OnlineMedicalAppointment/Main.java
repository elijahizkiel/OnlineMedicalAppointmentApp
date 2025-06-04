package com.example.OnlineMedicalAppointment;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.OnlineMedicalAppointment.database.DatabaseConnector;
import com.example.OnlineMedicalAppointment.database.DatabaseInitializer;
import com.example.OnlineMedicalAppointment.ui.LoginFrame;

/**
 * Main entry point for the Online Medical Appointment application.
 */
public class Main {
    public static Connection connection;

    /**
     * Application entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Online Medical Appointment App...");

        try {
            // Initialize database connection
            connection = DatabaseConnector.connect();
            System.out.println("Connected to SQLite database.");
            
            // Initialize database tables
            DatabaseInitializer.initializeDatabase(connection);
            System.out.println("Database initialized successfully.");
            
            // Initialize UI (Login Frame)
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    /**
     * Sets the database connection.
     * @param conn the connection to set
     */
    public static void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Gets the database connection.
     * @return the current connection
     */
    public static Connection getConnection() {
        return connection;
    }
}
