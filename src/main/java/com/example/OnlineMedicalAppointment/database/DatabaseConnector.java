package com.example.OnlineMedicalAppointment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:sqlite:online_medical_appointment.db";
    private static  Connection connection;

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
    public static Connection getConnection() throws SQLException {
        
        if (connection == null || connection.isClosed()) { // Check if connection is null or closed
            return connect();
        }
        return connection;
    }

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
