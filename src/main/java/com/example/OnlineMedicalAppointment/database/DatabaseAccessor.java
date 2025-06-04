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

/**
 * Abstract class providing static methods for database access and manipulation.
 */
public abstract class DatabaseAccessor {

    /**
     * Retrieves a list of doctors by specialty.
     * @param specialty the specialty to filter by
     * @return list of doctors with the given specialty
     */
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
    /**
     * Retrieves a list of all doctors.
     * @return list of all doctors
     */
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

    /**
     * Retrieves all messages for a given chat room.
     * @param roomId the chat room ID
     * @return list of messages in the chat room
     */
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
    
    /**
     * Retrieves all appointments for a user (doctor or patient).
     * @param userId the user ID
     * @return list of appointments
     */
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

    /**
     * Retrieves all chat room IDs for a user.
     * @param userId the user ID
     * @return list of chat room IDs
     */
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
    /**
     * Adds a user to the database.
     * @param user the user to add
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Updates a user in the database.
     * @param user the user to update
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user){
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
                return true;
            } else {
                System.out.println("User not found for update: " + user.getUsername());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a user from the database.
     * @param userID the user ID to delete
     */
    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM users_table WHERE userID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            System.out.println("User deleted successfully with ID: " + userID);
            return true;
        } else {
            System.out.println("User not found for deletion with ID: " + userID);   
            return false;
        }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a user by username.
     * @param username the username
     * @return the User object, or null if not found
     */
    public static User getByUsername(String username) {
        String query = "SELECT * FROM users_table WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
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
    /**
     * Retrieves a user by user ID.
     * @param userId the user ID
     * @return the User object, or null if not found
     */
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

    /**
     * Adds a message to the database.
     * @param message the message to add
     * @return true if successful, false otherwise
     */
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

    /**
     * Updates a message in the database.
     * @param message the message to update
     */
    public boolean updateMessage(Message message){
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
                return true;
            } else {
                System.out.println("Message not found for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating message: " + e.getMessage());
            return false;
        }
    }
    public boolean deleteMessage(int messageId){
        String sql = "DELETE FROM Messages WHERE messageID = ?";
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, messageId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                System.out.println("Message deleted successfully with ID: " + messageId);
                return true;
                } else {
                System.out.println("Message not found for deletion with ID: " + messageId);
                    return false;
            }
            } catch (SQLException e) {
                System.err.println("Error deleting message: " + e.getMessage());
            return false;
            }
            }
            
            /**
             * Adds an appointment to the database.
             * @param appointment the appointment to add
             * @return true if successful, false otherwise
             */
            public static boolean addAppointment(Appointment appointment){
                String sql = "INSERT INTO appointments (patientID, doctorID, appointmentTime, status) VALUES (?, ?, ?, ?)";
                try (Connection conn = DatabaseConnector.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // Convert LocalDateTime to Timestamp
                        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(appointment.getAppointmentTime());
                        pstmt.setInt(1, appointment.getPatientID());
                        pstmt.setInt(2, appointment.getDoctorID());
                        pstmt.setTimestamp(3, timestamp);
                        pstmt.setString(4, appointment.getStatus());
                        int affectedRows = pstmt.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Appointment added successfully.");
                            return true;
                        } else {
                            System.out.println("Failed to add appointment.");
                            return false;
                        }
                } catch (SQLException e) {
                    System.err.println("Error adding appointment: " + e.getMessage());
                    return false;
                }
            }

            /**
             * Updates an appointment in the database.
             * @param appointment the appointment to update
             * @return true if successful, false otherwise
             */
            public static boolean updateAppointment(Appointment appointment){
                String sql = "UPDATE appointments SET patientID = ?, doctorID = ?, appointmentTime = ?, status = ? WHERE scheduleID = ?";
                try (Connection conn = DatabaseConnector.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                     java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(appointment.getAppointmentTime());
                       
                    pstmt.setInt(1, appointment.getPatientID());
                    pstmt.setInt(2, appointment.getDoctorID());
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setString(4, appointment.getStatus());
                    pstmt.setInt(5, appointment.getScheduleID());
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Appointment updated successfully with ID: " + appointment.getScheduleID());
                        return true;
                    } else {
                        System.out.println("Appointment not found for update with ID: " + appointment.getScheduleID());
                        return false;
                    }
                } catch (SQLException e) {
                    System.err.println("Error updating appointment: " + e.getMessage());
                    return false;
                }
            }

            /**
             * Deletes an appointment from the database.
             * @param appointmentId the ID of the appointment to delete
             * @return true if successful, false otherwise
             */
            public static boolean deleteAppointment(int appointmentId){
                String sql = "DELETE FROM appointments WHERE scheduleID = ?";
                try (Connection conn = DatabaseConnector.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, appointmentId);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Appointment deleted successfully with ID: " + appointmentId);
                        return true;
                    } else {
                        System.out.println("Appointment not found for deletion with ID: " + appointmentId);
                        return false;
                    }
                } catch (SQLException e) {
                    System.err.println("Error deleting appointment: " + e.getMessage());
                    return false;
                }
            }
        }    

