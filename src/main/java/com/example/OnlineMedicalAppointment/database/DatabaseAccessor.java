package com.example.OnlineMedicalAppointment.database;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Message;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Doctor;

// DatabaseAccessor.java
public abstract class DatabaseAccessor {
    
    public static List<Doctor> getDoctorsBySpecialty(String specialty){
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' AND specialty = ?";
        
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection(); // Assume getConnection() is available
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specialty);
            try (ResultSet rs = stmt.executeQuery()) {
               while (rs.next()) {
                  Doctor user =  new Doctor(rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                        );
                  doctors.add(user);
               }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctors by specialty: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
        return doctors;
    }
    public static List<Doctor> getDoctors(){
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' ";
        
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()){
               for (int i = 0; i < 10; i++) {
                    if (!rs.next()) {
                        break; // Exit loop if no more results
                    }
               if (rs.next()) {
                  Doctor user =  new Doctor(rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                        );
                  doctors.add(user);
                  System.out.println("Doctor added: " + user.getFName() + " " + user.getLName());
               }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctors: " + e.getMessage());
        }
        return doctors;
    }

    public static List<Message> getMessages(String roomId){
        String sql = "SELECT * FROM messages WHERE roomID = ?";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("messageId"),
                        rs.getInt("roomID"),
                        rs.getInt("senderId"),
                        rs.getInt("receiverId"),
                        rs.getString("messageText"),
                        rs.getTimestamp("timestamp")
                        );
                    
                    // fetch and set the message sender name
                    PreparedStatement senderquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    senderquery.setInt(1, message.getSenderId());
                    ResultSet senderName = senderquery.executeQuery();
                    message.setSenderName(senderName.getString("FName") + " " + senderName.getString("LName"));

                    //fetch and set the message receiver name
                    PreparedStatement receiverquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    receiverquery.setInt(1, message.getReceiverId());
                    ResultSet receiverName = receiverquery.executeQuery();
                    message.setReceiverName(receiverName.getString("FName") + " " + receiverName.getString("LName"));
                    messages.add(message);
                }
            }
        } catch (SQLException e) {   
            System.out.println("SQL Exception thrown while getting messages: " + e.getMessage());
        }
        return messages;
    };
    
    public static List<Appointment> getAppointments(int userId){
        String sql = "SELECT * FROM appointments WHERE doctorID = ? OR patientID = ?";

        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment(
                        rs.getInt("scheduleID"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorId"),
                        rs.getLong("appointmentTime"),
                        rs.getString("status"));
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLExceprion thrown while getting appointment: " + e.getMessage());
        }
        return appointments;

    };

    public static List<String> getChatRoomID(int userId) {
        String sql = "SELECT roomID FROM messages WHERE senderID = ? OR receiverID = ? GROUP BY roomID";
        List<String> roomIds = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    roomIds.add(rs.getString("roomID"));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting chat room IDs: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
        return roomIds;
    }
    // Additional methods for adding, updating, and deleting records
    public static boolean addUser(User user){
        String sql = "INSERT INTO users (FName, LName, username, password, userType, specialty, phoneNumber) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFName());
            pstmt.setString(2, user.getLName());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getUserType());
            pstmt.setString(6, user.getSpecialty());
            pstmt.setString(7, user.getPhoneNumber());
            pstmt.executeUpdate();
            System.out.println("User added successfully: " + user.getUsername());
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }
    
    public void updateUser(User user){
        String sql = "UPDATE users_table SET FName = ?, LName = ?, username = ?, password = ?, userType = ?, specialty = ?, phoneNumber = ? WHERE userID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFName());
            pstmt.setString(2, user.getLName());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getUserType());
            pstmt.setString(6, user.getSpecialty());
            pstmt.setString(7, user.getPhoneNumber());
            pstmt.setInt(8, user.getUserID());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully: " + user.getUsername());
            } else {
                System.out.println("User not found for update: " + user.getUsername());
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int userID) {
        String sql = "DELETE FROM users_table WHERE userID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            System.out.println("User deleted successfully with ID: " + userID);
            } else {
            System.out.println("User not found for deletion with ID: " + userID);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }
    
    public static User getByUsername(int username) {
        String query = "SELECT * FROM users_table WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userType = rs.getString("userType");
                if (userType.equals("Patient")) {
                        return new Patient(
                            rs.getInt("userID"),
                            rs.getString("FName"),
                            rs.getString("LName"),
                            rs.getString("username"),
                            userType,
                            rs.getString("phoneNumber")
                        );
                    }else if(userType.equals("Doctor")){
                        Doctor doctor = new Doctor(rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        userType,
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                        );
                        return doctor;
                    }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient by username: " + e.getMessage());
        }
        
        return null;
    }
    public static User getUserByID(int userId) {
        String query = "SELECT * FROM users_table WHERE userID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userType = rs.getString("userType");
                if (userType.equals("Patient")) {
                        return new Patient(
                            rs.getInt("userID"),
                            rs.getString("FName"),
                            rs.getString("LName"),
                            rs.getString("username"),
                            userType,
                            rs.getString("phoneNumber")
                        );
                    }else if(userType.equals("Doctor")){
                        Doctor doctor = new Doctor(rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        userType,
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                        );
                        return doctor;
                    }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient by ID: " + e.getMessage());
        }   
        return null;
    }

    public boolean  addMessage(Message message){
        String sql = "INSERT INTO Messages (roomID, senderId, receiverId, messageText, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, message.getRoomID());
            pstmt.setInt(2, message.getSenderId());
            pstmt.setInt(3, message.getReceiverId());
            pstmt.setString(4, message.getMessage());
            pstmt.setTimestamp(5, message.getTimestamp());
            pstmt.executeUpdate();
            System.out.println("Message added successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding message: " + e.getMessage());
            return false;
        }
    }
    public void updateMessage(Message message){
        String sql = "UPDATE Messages SET roomID = ?, senderId = ?, receiverId = ?, messageText = ?, timestamp = ? WHERE messageID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, message.getRoomID());
            pstmt.setInt(2, message.getSenderId());
            pstmt.setInt(3, message.getReceiverId());
            pstmt.setString(4, message.getMessage());
            pstmt.setTimestamp(5, message.getTimestamp());
            pstmt.setInt(6, message.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Message updated successfully.");
            } else {
                System.out.println("Message not found for update.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating message: " + e.getMessage());
        }
    }
    public void deleteMessage(int messageId){}
    
    public void addAppointment(Appointment appointment){}
    public void updateAppointment(Appointment appointment){}
    public void deleteAppointment(int appointmentId){}
}

