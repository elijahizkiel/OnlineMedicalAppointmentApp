package com.example.OnlineMedicalAppointment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public class DatabaseConnector {
    private static final String DB_URL = "jdbc:sqlite:online_medical_appointment.db";
    private static  Connection connection;

    /**
     * Establishes a new connection to the SQLite database.
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found."+ e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection to SQLite failed."+ e.getMessage());
        }
        connection = conn;
        return conn;
    }
    /**
     * Gets the current database connection, or creates a new one if needed.
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        
        if (connection == null || connection.isClosed()) { // Check if connection is null or closed
            return connect();
        }
        return connection;
    }

    /**
     * Closes the given database connection.
     * @param conn the connection to close
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
