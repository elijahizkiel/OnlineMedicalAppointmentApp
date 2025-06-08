package com.example.OnlineMedicalAppointment.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.OnlineMedicalAppointment.model.Admin;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Message;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Abstract class providing static methods for database access and manipulation.
 * This class interacts with the underlying database to perform CRUD operations
 * for users, appointments, and messages, as well as providing data for admin statistics.
 */
public abstract class DatabaseAccessor {

    /**
     * Retrieves a list of doctors by specialty.
     *
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
                        rs.getString("password"),
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
     *
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
                        rs.getString("password"),
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
     * Retrieves a list of doctors by name (first or last).
     *
     * @param name the name to search for
     * @return list of doctors matching the name
     */
    public static List<Doctor> getDoctorsByName(String name) {
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' AND (FName LIKE ? OR LName LIKE ?)";
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                    doctors.add(doctor);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctors by name: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Retrieves a list of doctors by name and specialty.
     *
     * @param name the name to search for
     * @param specialty the specialty to filter by
     * @return list of doctors matching the name and specialty
     */
    public static List<Doctor> getDoctorsByNameAndSpecialty(String name, String specialty) {
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' AND (FName LIKE ? OR LName LIKE ?) AND specialty = ?";
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, "%" + name + "%");
            pstmt.setString(3, specialty);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                    doctors.add(doctor);
                    System.out.println("Doctor added: " + doctor.getFName() + " " + doctor.getLName());
                }
                if(rs.getFetchSize() == 0){
                    System.out.println("No Doctor is found");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctors by name and specialty: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Retrieves a list of doctors by name or specialty.
     *
     * @param query the name or specialty to search for
     * @return list of doctors matching the name or specialty
     */
    public static List<Doctor> getDoctorsByNameOrSpecialty(String query){
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' AND (FName = ? OR LName = ? OR specialty = ?)";
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Doctors found(from DBAccessor): " + rs.getFetchSize());
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                    doctors.add(doctor);
                    System.out.println(doctor.toString());
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctor by name or specialty: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Searches for doctors based on a query string that can match name or specialty.
     * Uses partial matching for more flexible search results and ranks results based on match quality.
     *
     * @param query The search query for name or specialty
     * @return List of matching doctors sorted by relevance
     */
    public static List<Doctor> searchDoctors(String query) {
        String sql = "SELECT *, " +
            "CASE " +
            "    WHEN FName LIKE ? OR LName LIKE ? THEN 3 " +
            "    WHEN specialty LIKE ? THEN 2 " +
            "    ELSE 1 " +
            "END as relevance " +
            "FROM users_table " +
            "WHERE userType = 'Doctor' " +
            "AND (FName LIKE ? OR LName LIKE ? OR specialty LIKE ?) " +
            "ORDER BY relevance DESC, FName ASC";
        
        List<Doctor> doctors = new ArrayList<>();
        String searchPattern = "%" + query + "%";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set parameters for relevance check
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            // Set parameters for actual search
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);
            pstmt.setString(6, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                    doctors.add(doctor);
                    System.out.println("Doctor found: " + doctor.getFName() + " " + doctor.getLName() + ", Relevance: " + rs.getInt("relevance"));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while searching doctors: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Retrieves a list of all appointments.
     *
     * @return list of all appointments
     */
    public static List<Appointment> getAllAppointments(){
        String sql = "SELECT * FROM Schedules "; // Changed table name to 'appointments' for consistency    
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            ResultSet rs = stmt.executeQuery()){ // Execute the query
            
               while (rs.next()) { // Iterate through the results
                  Appointment appointment =  new Appointment(
                        rs.getInt("scheduleID"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorID"),
                        rs.getTimestamp("appointmentTime").toLocalDateTime(), // Assuming appointmentTime is stored as a long (timestamp)
                        rs.getTimestamp("bookedOn").toLocalDateTime(), // Assuming bookedOn is stored as a timestamp
                        rs.getString("status")
                        );
                  appointments.add(appointment);
               }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting all appointments: " + e.getMessage());
            return new ArrayList<>(); 
        }
        return appointments;
    }
    /**
     * Retrieves a doctor by name and specialty.
     *
     * @param name the name to search for
     * @param specialty the specialty to filter by
     * @return the Doctor object if found, null otherwise
     */
    public static Doctor getDoctorByNameAndSpecialty(String name, String specialty) {
        String sql = "SELECT * FROM users_table WHERE userType = 'Doctor' AND (FName = ? OR LName = ?) AND specialty = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, name);
            pstmt.setString(3, specialty);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting doctor by name and specialty: " + e.getMessage());
        }
        return null; // Return null if no doctor found
    }

    /**
     * Retrieves all messages for a given chat room.
     *
     * @param roomId the chat room ID
     * @return list of messages in the chat room
     */
    public static List<Message> getMessages(String roomId){
        String sql = "SELECT * FROM messages WHERE roomID = ? ORDER BY timestamp ASC"; // Added ORDER BY
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("messageId"),
                        rs.getString("roomID"),
                        rs.getInt("senderId"),
                        rs.getInt("receiverId"),
                        rs.getString("messageText"),
                        rs.getTimestamp("timestamp")
                        );

                    // fetch and set the message sender name
                    PreparedStatement senderquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    senderquery.setInt(1, message.getSenderId());
                    ResultSet senderNameRs = senderquery.executeQuery(); // Use a different variable name
                    if (senderNameRs.next()) {
                         message.setSenderName(senderNameRs.getString("FName") + " " + senderNameRs.getString("LName"));
                    } else {
                         message.setSenderName("Unknown Sender");
                    }
                    senderNameRs.close(); // Close the inner ResultSet

                    //fetch and set the message receiver name
                    PreparedStatement receiverquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    receiverquery.setInt(1, message.getReceiverId());
                    ResultSet receiverNameRs = receiverquery.executeQuery(); // Use a different variable name
                    if (receiverNameRs.next()) {
                        message.setReceiverName(receiverNameRs.getString("FName") + " " + receiverNameRs.getString("LName"));
                    } else {
                        message.setReceiverName("Unknown Receiver");
                    }
                    receiverNameRs.close(); // Close the inner ResultSet

                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting messages: " + e.getMessage());
        }
        return messages;
    };

    /**
     * Retrieves all messages for a specific doctor.
     * This method assumes messages are directed to a doctor user ID.
     *
     * @param doctorId the doctor's user ID
     * @return list of messages received by the doctor
     */
    public static List<Message> getMessagesForDoctor(int doctorId) {
        String sql = "SELECT * FROM messages WHERE receiverId = ? ORDER BY timestamp ASC"; // Added ORDER BY
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("messageId"),
                        rs.getString("roomID"),
                        rs.getInt("senderId"),
                        rs.getInt("receiverId"),
                        rs.getString("messageText"),
                        rs.getTimestamp("timestamp")
                    );

                    // fetch and set the message sender name
                    PreparedStatement senderquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    senderquery.setInt(1, message.getSenderId());
                    ResultSet senderNameRs = senderquery.executeQuery(); // Use a different variable name
                    if (senderNameRs.next()) {
                         message.setSenderName(senderNameRs.getString("FName") + " " + senderNameRs.getString("LName"));
                    } else {
                         message.setSenderName("Unknown Sender");
                    }
                    senderNameRs.close(); // Close the inner ResultSet

                    //fetch and set the message receiver name
                    PreparedStatement receiverquery= conn.prepareStatement("SELECT FName, LName FROM users_table WHERE userID = ?");
                    receiverquery.setInt(1, message.getReceiverId());
                    ResultSet receiverNameRs = receiverquery.executeQuery(); // Use a different variable name
                    if (receiverNameRs.next()) {
                        message.setReceiverName(receiverNameRs.getString("FName") + " " + receiverNameRs.getString("LName"));
                    } else {
                        message.setReceiverName("Unknown Receiver");
                    }
                    receiverNameRs.close(); // Close the inner ResultSet

                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting messages for doctor: " + e.getMessage());
        }
        return messages;
    }


    /**
     * Retrieves all appointments for a user (doctor or patient).
     *
     * @param userId the user ID
     * @return list of appointments
     */
    public static List<Appointment> getAppointments(int userId){
        String sql = "SELECT * FROM Schedules WHERE doctorID = ? OR patientID = ?";
        System.out.println("Getting appointments for user ID: " + userId);
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment(
                        rs.getInt("scheduleID"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorId"),
                        rs.getTimestamp("appointmentTime").toLocalDateTime(),
                        rs.getTimestamp("bookedOn").toLocalDateTime(),// Assuming bookedOn is stored as a timestamp;
                        rs.getString("status"));
                    appointments.add(appointment);
                    System.out.println(appointment.toString());
                }
                if(rs.getFetchSize() == 0){
                    System.out.println("No appointment for userID:" + userId);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("SQLException thrown while getting appointment by userID: " + e.getMessage());
        }
        return appointments;

    };

    /**
     * Retrieves all chat room IDs for a user.
     *
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
            if(!roomIds.isEmpty()) roomIds.removeFirst();
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while getting chat room IDs: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
        return roomIds;
    }

    /**
     * Retrieves the chat room ID between two specific users.
     * Creates a new room ID if one does not exist.
     * The room ID is generated based on the sorted user IDs to ensure uniqueness
     * regardless of which user initiates the chat.
     * @param userId1 The ID of the first user.
     * @param userId2 The ID of the second user.
     * @return The chat room ID string.
     */
    public static String getChatRoomIdBetweenUsers(int userId1, int userId2) {
        // Ensure consistent room ID regardless of which user is userId1 or userId2
        String roomIdentifier = (userId1 < userId2) ? userId1 + "_" + userId2 : userId2 + "_" + userId1;

        // In a real database, you might have a dedicated chat_rooms table
        // and look up the room ID there, creating it if it doesn't exist.
        // For this example, we'll use a simple approach: check if any message
        // exists with a roomID matching the identifier. If not, assume the room
        // doesn't exist yet and the first message will implicitly create it
        // (or you could explicitly insert into a chat_rooms table here).

        // Simple check if any message exists for this room identifier
        String sqlCheck = "SELECT roomID FROM messages WHERE roomID = ? LIMIT 1";
         try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, roomIdentifier);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("roomID"); // Room exists
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking for existing chat room: " + e.getMessage());
            // Fallback: proceed assuming room might not exist, or handle error
        }

        // If no message found, return the generated identifier.
        // The first message added with this ID will establish the room.
        return roomIdentifier; // Or generate a UUID if preferred: UUID.randomUUID().toString();
    }


    // Additional methods for adding, updating, and deleting records
    /**
     * Adds a user to the database.
     *
     * @param user the user to add
     * @return true if successful, false otherwise
     */
    public static boolean addUser(User user){
        String sql = "INSERT INTO users_table (FName, LName, username, password, userType, specialty, phoneNumber) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
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
     * Retrieves the count of users by their type.
     *
     * @param type The type of user (e.g., "Patient", "Doctor", "Admin").
     * @return The count of users of the specified type, or 0 if an error occurs.
     */
    public static int getUsersCountByType(String type){
        String sql = "SELECT COUNT(*) FROM users_table WHERE userType = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user count by type: " + e.getMessage());
        }
        return 0;
    }
    /**
     * Updates a user in the database.
     *
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
     *
     * @param userID the user ID to delete
     * @return true if successful, false otherwise
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
     *
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
                        rs.getString("password"),
                        rs.getString("userType"),
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
     *
     * @param userId the user ID
     * @return the User object, or null if not found
     */
    public static User getUserByID(int userId) {
        /**
         * Retrieves a user by their ID.
         *
         * @param userId the ID of the user to retrieve
         * @return the User object if found, null otherwise
         */
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
                        rs.getString("password"),
                        rs.getString("userType"),
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

    public static List<User> getUsersByType(String type){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users_table WHERE userType = ?";
        try(PreparedStatement pstmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user;
                if (type.equals("Patient")) {
                    user = new Patient(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        type,
                        rs.getString("phoneNumber")
                    );
                } else if (type.equals("Doctor")) {
                    user = new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        type,
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    );
                } else {
                    continue; // Skip unsupported user types
                }
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users by type: " + e.getMessage());
        }
        return users;
    }
    /**
     * Adds a message to the database.
     *
     * @param message the message to add
     * @return true if successful, false otherwise
     */
    public static boolean addMessage(Message message){
        String sql = "INSERT INTO Messages (roomID, senderId, receiverId, messageText, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getRoomID());
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
     *
     * @param message the message to update
     * @return true if successful, false otherwise
     */
    public boolean updateMessage(Message message){
        String sql = "UPDATE Messages SET roomID = ?, senderId = ?, receiverId = ?, messageText = ?, timestamp = ? WHERE messageID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getRoomID());
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
    /**
     * Deletes a message from the database.
     *
     * @param messageId the ID of the message to delete
     * @return true if successful, false otherwise
     */
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
             *
             * @param appointment the appointment to add
             * @return true if successful, false otherwise
             */
    public static boolean addAppointment(Appointment appointment){
                String sql = "INSERT INTO Schedules (patientID, doctorID, bookedOn, appointmentTime, status) VALUES (?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseConnector.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // Convert LocalDateTime to Timestamp
                        java.sql.Timestamp appointmentTimestamp = java.sql.Timestamp.valueOf(appointment.getAppointmentTime());
                        java.sql.Timestamp bookedOnTimestamp = java.sql.Timestamp.valueOf(appointment.getBookedOn());
                        pstmt.setInt(1, appointment.getPatientID());
                        pstmt.setInt(2, appointment.getDoctorID());
                        pstmt.setTimestamp(3, bookedOnTimestamp);
                        pstmt.setTimestamp(4, appointmentTimestamp);
                        pstmt.setString(5, appointment.getStatus());
                        int affectedRows = pstmt.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Appointment added successfully.");
                            System.out.println("Appointment time: " + appointmentTimestamp.toString() +"\n booked on: " + bookedOnTimestamp.toString());
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
    *
    * @param appointment the appointment to update
    * @return true if successful, false otherwise
    */
    public static boolean updateAppointment(Appointment appointment){
                String sql = "UPDATE Schedules SET patientID = ?, doctorID = ?, appointmentTime = ?, status = ? WHERE scheduleID = ?";
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
             *
             * @param appointmentId the ID of the appointment to delete
             * @return true if successful, false otherwise
             */
    public static boolean deleteAppointment(int appointmentId){
                String sql = "DELETE FROM Schedules WHERE scheduleID = ?";
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

    // --- Admin Dashboard Statistics ---
    /**
     * Retrieves the count of user registrations grouped by month.
     *
     * @return A map where keys are month strings (YYYY-MM) and values are the count of registrations.
     */
    public static Map<String, Integer> getUserRegistrationsByMonth() {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT strftime('%Y-%m', rowid) as month, COUNT(*) as count FROM users_table GROUP BY month ORDER BY month DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting user registrations by month: " + e.getMessage());
        }
        return result;
    }
    /**
     * Retrieves the count of appointments grouped by month.
     *
     * @return A map where keys are month strings (YYYY-MM) and values are the count of appointments.
     */
    public static Map<String, Integer> getAppointmentsByMonth() {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT strftime('%Y-%m', appointmentTime) as month, COUNT(*) as count " +
                     "FROM Schedules GROUP BY month ORDER BY month DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments by month: " + e.getMessage());
        }
        return result;
    }
    /**
     * Retrieves the top N doctors based on the number of appointments.
     *
     * @param topN The number of top doctors to retrieve.
     * @return A list of Doctor objects.
     */
    public static List<Doctor> getMostActiveDoctors(int topN) {
        List<Doctor> result = new ArrayList<>();
        String sql = "SELECT doctorID, COUNT(*) as count FROM Schedules GROUP BY doctorID ORDER BY count DESC LIMIT ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, topN);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Doctor d = (Doctor) getUserByID(rs.getInt("doctorID"));
                    result.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting most active doctors: " + e.getMessage());
        }
        return result;
    }
    /**
     * Retrieves the top N patients based on the number of appointments booked.
     *
     * @param topN The number of top patients to retrieve.
     * @return A list of Patient objects.
     */
    public static List<Patient> getMostActivePatients(int topN) {
        List<Patient> result = new ArrayList<>();
        String sql = "SELECT patientID, COUNT(*) as count FROM Schedules GROUP BY patientID ORDER BY count DESC LIMIT ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, topN);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient p = (Patient) getUserByID(rs.getInt("patientID"));
                    result.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting most active patients: " + e.getMessage());
        }
        return result;
            }
    
    // Admin Dashboard Methods
    
    /**
     * Gets total appointments grouped by status
     */
    public static List<Object[]> getTotalAppointmentsByStatus() {
        String sql = "SELECT status, COUNT(*) as count FROM Schedules GROUP BY status";
        return executeQuery(sql);
    }
    
    /**
     * Gets top doctors by appointment status
     * 
     * @param status Appointment status to filter by
     * @param limit Number of results to return
     */
    public static List<Object[]> getTopDoctorsByAppointmentStatus(String status, int limit) {
        String sql = "SELECT d.FName || ' ' || d.LName as doctorName, COUNT(*) as count " +
                     "FROM Schedules s " +
                     "JOIN users_table d ON s.doctorID = d.userID " +
                     "WHERE s.status = ? " +
                     "GROUP BY doctorName ORDER BY count DESC LIMIT ?";
        return executeQuery(sql, status, limit);
    }
    
    /**
     * Gets daily appointment counts
     * 
     * @param days Number of past days to include
     */
    public static List<Object[]> getDailyAppointmentsCount(int days) {
        String sql = "SELECT DATE(appointmentTime) as appointmentDate, COUNT(*) as count " +
                     "FROM Schedules " +
                     "WHERE appointmentTime >= DATE('now', '-' || ? || ' days') " +
                     "GROUP BY appointmentDate ORDER BY appointmentDate ASC";
        return executeQuery(sql, days);
    }
    
    /**
     * Tracks appointment status changes over time
     */
    public static List<Object[]> getAppointmentStatusOverTime() {
        String sql = "SELECT DATE(appointmentTime) as date, status, COUNT(*) as count " +
                     "FROM Schedules " +
                     "GROUP BY date, status ORDER BY date ASC";
        return executeQuery(sql);
    }

    /**
     * Gets appointments made per day
     * 
     * @param days Number of past days to include
     */
    public static Map<String, Integer> getAppointmentsMadeByDay(int days) {
        List<Object[]> results = executeQuery(
            "SELECT DATE(bookedOn) as date, COUNT(*) as count " +
            "FROM Schedules " +
            "WHERE bookedOn >= DATE('now', '-' || ? || ' days') " +
            "GROUP BY date ORDER BY date ASC", 
            days
        );
        return convertToMap(results);
    }
    
    /**
     * Gets appointments held per day
     * 
     * @param days Number of past days to include
     */
    public static Map<String, Integer> getAppointmentsHeldByDay(int days) {
        List<Object[]> results = executeQuery(
            "SELECT DATE(appointmentTime) as date, COUNT(*) as count " +
            "FROM schedule_table " +
            "WHERE status = 'Held' " +
            "AND appointmentTime >= DATE('now', '-' || ? || ' days') " +
            "GROUP BY date ORDER BY date ASC", 
            days
        );
        return convertToMap(results);
    }
    
    /**
     * Converts query results (List of Object arrays) to a Map<String, Integer>.
     * Assumes the first column is the key (String) and the second is the value (Number).
     * 
     * @param results The list of query results.
     * @return A map converted from the results.
     */
    private static Map<String, Integer> convertToMap(List<Object[]> results) {
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : results) {
            if (row.length >= 2) {
                String date = row[0].toString();
                int count = ((Number) row[1]).intValue();
                map.put(date, count);
            }
        }
        return map;
    }
    /**
     * Helper method to execute SQL queries with optional parameters and return results.
     * 
     * @param sql The SQL query string.
     * @param params Optional parameters for the prepared statement.
     * @return A list of Object arrays representing the query results.
     */
    private static List<Object[]> executeQuery(String sql, Object... params) {
        List<Object[]> results = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception thrown while executing query: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves appointments for a specific doctor on a given date.
     *
     * @param doctorId The ID of the doctor.
     * @param date The date to filter appointments by.
     * @return A list of Appointment objects for the specified doctor and date.
     */
    public static List<Appointment> getAppointmentsForDoctorOnDate(int doctorId, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Schedules WHERE doctorID = ? AND DATE(appointmentTime) = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setString(2, date.toString()); // LocalDate in ISO format (yyyy-MM-dd)
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment(
                        rs.getInt("scheduleID"),
                        rs.getInt("patientID"),
                        rs.getInt("doctorID"),
                        rs.getTimestamp("appointmentTime").toLocalDateTime(),
                        rs.getTimestamp( "bookedOn").toLocalDateTime(), // Assuming bookedOn is stored as a timestamp
                        rs.getString("status")
                    );
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments for doctor on date: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Retrieves the full name of a patient by their user ID.
     *
     * @param patientId The ID of the patient.
     * @return The full name of the patient (FName LName), or null if not found.
     */
    public static String getPatientName(int patientId) {
        String sql = "SELECT FName, LName FROM users_table WHERE userID = ? AND userType = 'Patient'";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("FName") + " " + rs.getString("LName");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting patient name: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a list of all users (patients, doctors, and admins).
     *
     * @return list of all users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users_table";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String userType = rs.getString("userType");
                if (userType.equals("Patient")) {
                    users.add(new Patient(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phoneNumber")
                    ));
                } else if (userType.equals("Doctor")) {
                    users.add(new Doctor(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                    ));
                } else if (userType.equals("Admin")) {
                    users.add(new Admin(
                        rs.getInt("userID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phoneNumber")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }
}
