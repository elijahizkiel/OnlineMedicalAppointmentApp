package com.example;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Map;

public class AuthService {

    public boolean signUpUser(String firstName, String lastName, String username, String password, String userType, String specialty) {
        if (DatabaseManager.getUserByUsername(username) != null) {
            System.out.println("Username already exists.");
            return false; // Username already exists
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // For Doctor type, specialty is mandatory. For others, it can be null.
        if ("Doctor".equals(userType) && (specialty == null || specialty.trim().isEmpty())) {
            System.out.println("Specialty is required for Doctor user type.");
            return false; 
        }
        if (!"Doctor".equals(userType)) {
            specialty = null; // Ensure specialty is null for non-doctors
        }

        return DatabaseManager.insertUser(firstName, lastName, username, hashedPassword, userType, specialty);
    }

    public User loginUser(String username, String password) {
        Map<String, Object> userData = DatabaseManager.getUserByUsername(username);

        if (userData != null && BCrypt.checkpw(password, (String) userData.get("password"))) {
            // Passwords match
            return new User(
                (Integer) userData.get("userID"),
                (String) userData.get("username"),
                (String) userData.get("userType"),
                (String) userData.get("FName"),
                (String) userData.get("LName"),
                (String) userData.get("specialty")
            );
        }
        return null; // User not found or password incorrect
    }

    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        User user = DatabaseManager.getUserById(userId);
        if (user == null) {
            System.err.println("Change password failed: User not found.");
            return false;
        }

        // Need to get the hashed password from DB to verify currentPassword
        // The User object returned by getUserById should ideally contain the hashed password
        // For this, we might need to adjust what DatabaseManager.getUserById returns or add a specific method
        // Let's assume DatabaseManager.getUserByUsername(user.username) also fetches the hashed password
        // or that getUserById returns it in a map or similar that AuthService can access.
        // For now, let's assume User object from getUserById has the password field directly from DB (which is hashed).
        Map<String, Object> userData = DatabaseManager.getUserByUsername(user.username); // Re-fetch to get raw password field
        
        if (userData == null || !BCrypt.checkpw(currentPassword, (String) userData.get("password"))) {
            System.err.println("Change password failed: Current password incorrect.");
            return false;
        }

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return DatabaseManager.updateUserPassword(userId, newHashedPassword);
    }
}

// Simple User class to hold user data after login
class User {
    int userID;
    String username;
    String userType;
    String firstName;
    String lastName;
    String specialty;
    String phoneNumber; // Added phoneNumber

    public User(int userID, String username, String userType, String firstName, String lastName, String specialty) {
        this(userID, username, userType, firstName, lastName, specialty, null); // Call overloaded constructor
    }

    public User(int userID, String username, String userType, String firstName, String lastName, String specialty, String phoneNumber) {
        this.userID = userID;
        this.username = username;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber; // Initialize phoneNumber
    }

    // Getter for phoneNumber if needed, e.g. by ProfileView if not directly accessing fields
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "N/A") + '\'' +
                '}';
    }
}
