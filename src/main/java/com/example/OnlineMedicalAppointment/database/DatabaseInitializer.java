package com.example.OnlineMedicalAppointment.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for initializing the database schema.
 * Creates necessary tables if they do not already exist.
 */
public class DatabaseInitializer {

    /**
     * Initializes the database tables if they do not exist.
     * @param connection the database connection
     */
    public static void initializeDatabase(Connection connection) {
        try {
            Statement statement = connection.createStatement();

            // Create users_table
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users_table (" +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "FName VARCHAR(255) NOT NULL," +
                    "LName VARCHAR(255) NOT NULL," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "userType VARCHAR(50) NOT NULL," +
                    "specialty VARCHAR(255)," +
                    "phoneNumber VARCHAR(20)" +
                    ")";
            statement.executeUpdate(createUserTableSQL);

            // Create Messages table
            String createMessagesTableSQL = "CREATE TABLE IF NOT EXISTS Messages (" +
                    "messageID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "senderID INTEGER NOT NULL," +
                    "roomID STRING NOT NULL," +
                    "receiverID INTEGER NOT NULL," +
                    "messageText TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (senderID) REFERENCES users_table(userID)," +
                    "FOREIGN KEY (receiverID) REFERENCES users_table(userID)" +
                    ")";
            statement.executeUpdate(createMessagesTableSQL);

            // Create Schedules table
            String createSchedulesTableSQL = "CREATE TABLE IF NOT EXISTS Schedules (" +
                    "scheduleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patientID INTEGER NOT NULL," +
                    "doctorID INTEGER NOT NULL," +
                    "bookedOn TIMESTAMP NOT NULL," +
                    "appointmentTime TIMESTAMP NOT NULL," +
                    "status TEXT CHECK (status IN ('Pending','Approved', 'Rejected', 'Cancelled', 'Held')), " +
                    "FOREIGN KEY (doctorID) REFERENCES users_table(userID), " +
                    "FOREIGN KEY (patientID) REFERENCES users_table(userID)" +
                    ")";
            statement.executeUpdate(createSchedulesTableSQL);

            System.out.println("Database tables created successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
