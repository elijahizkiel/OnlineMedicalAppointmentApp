package com.example.OnlineMedicalAppointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.OnlineMedicalAppointment.database.DatabaseConnector;
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.Admin;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
public class AuthService {

    public static User login(String username, String password) {
        String sql = "SELECT * FROM users_table WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                switch (rs.getString("userType")) {
                    case "Admin":
                        System.out.println("Admin logged in");
                        return new Admin(rs.getInt("userID"),
                                    rs.getString("FName"),
                                    rs.getString("LName"),
                                    rs.getString("username"),
                                    rs.getString("phoneNumber"));
                    case "Doctor":
                        System.out.println("Doctor logged in");
                        return new Doctor(rs.getInt("userID"),
                                    rs.getString("FName"),
                                    rs.getString("LName"),
                                    rs.getString("username"),
                                    rs.getString("userType"),
                                    rs.getString("specialty"),
                                    rs.getString("phoneNumber"));
                        
                    case "Patient":
                        System.out.println("Patient logged in");
                        return new Patient(rs.getInt("userID"),
                                    rs.getString("FName"),
                                    rs.getString("LName"),
                                    rs.getString("username"),
                                    rs.getString("phoneNumber"));
                        
                    default:
                    System.out.println("Unknown user type");
                }
                
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean signup(String firstName, String lastName, String username, String password, String userType, String specialty, String phoneNumber) {
        User user;
        if(userType.equals("Doctor")){
            user = new Doctor( firstName, lastName, username, password, userType, specialty, phoneNumber);
             return DatabaseAccessor.addUser(user);
        } else if(userType.equals("Patient")){
            user = new Patient(firstName, lastName, username, password, phoneNumber);
             return DatabaseAccessor.addUser(user);
        }else{
            System.out.println("Invalid user type for signup");
            return false;
        }
    }
}
