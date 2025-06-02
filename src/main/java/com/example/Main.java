package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize database first
        System.out.println("Initializing database...");
        DatabaseManager.initializeDatabase();
        System.out.println("Database initialization complete.");

        // Schedule AuthView to be created and shown on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AuthView().setVisible(true);
            }
        });
    }
}
