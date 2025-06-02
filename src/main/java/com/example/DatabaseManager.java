package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:medical_app.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users_table (" +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "FName TEXT NOT NULL," +
                    "LName TEXT NOT NULL," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "userType TEXT NOT NULL CHECK(userType IN ('Doctor', 'Patient', 'Admin'))," +
                    "specialty TEXT," +
                    "phoneNumber TEXT" +
                    ");";
            stmt.execute(createUsersTableSQL);
            System.out.println("users_table created successfully or already exists.");

            String createMessagesTableSQL = "CREATE TABLE IF NOT EXISTS Messages (" +
                    "messageID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "senderID INTEGER NOT NULL," +
                    "receiverID INTEGER," +
                    "messageContent TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (senderID) REFERENCES users_table(userID)," +
                    "FOREIGN KEY (receiverID) REFERENCES users_table(userID)" +
                    ");";
            stmt.execute(createMessagesTableSQL);
            System.out.println("Messages table created successfully or already exists.");

            String createSchedulesTableSQL = "CREATE TABLE IF NOT EXISTS Schedules (" +
                    "scheduleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "doctorID INTEGER NOT NULL," +
                    "patientID INTEGER," +
                    "startTime DATETIME NOT NULL," +
                    "endTime DATETIME NOT NULL," +
                    "isBooked BOOLEAN DEFAULT FALSE," +
                    "FOREIGN KEY (doctorID) REFERENCES users_table(userID)," +
                    "FOREIGN KEY (patientID) REFERENCES users_table(userID)" +
                    ");";
            stmt.execute(createSchedulesTableSQL);
            System.out.println("Schedules table created successfully or already exists.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static Map<String, Object> getUserByUsername(String username) {
        String sql = "SELECT userID, username, password, userType, FName, LName, specialty FROM users_table WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("userID", rs.getInt("userID"));
                user.put("username", rs.getString("username"));
                user.put("password", rs.getString("password"));
                user.put("userType", rs.getString("userType"));
                user.put("FName", rs.getString("FName"));
                user.put("LName", rs.getString("LName"));
                user.put("specialty", rs.getString("specialty"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        }
        return null;
    }

    public static boolean insertUser(String fName, String lName, String username, String hashedPassword, String userType, String specialty) {
        if (getUserByUsername(username) != null) {
            System.err.println("Username '" + username + "' already exists.");
            return false;
        }
        String sql = "INSERT INTO users_table(FName, LName, username, password, userType, specialty) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, username);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, userType);
            if (specialty != null && "Doctor".equals(userType)) {
                pstmt.setString(6, specialty);
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                 System.err.println("Error inserting user: Username '" + username + "' already exists. (Caught by DB constraint)");
            } else {
                System.err.println("Error inserting user: " + e.getMessage());
            }
            return false;
        }
    }

    public static List<User> getAllDoctors() {
        List<User> doctors = new ArrayList<>();
        String sql = "SELECT userID, FName, LName, specialty, username, userType FROM users_table WHERE userType = 'Doctor'";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("userType"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all doctors: " + e.getMessage());
        }
        return doctors;
    }

    public static List<AppointmentDetails> getAppointmentsForPatient(int patientId) {
        List<AppointmentDetails> appointments = new ArrayList<>();
        String sql = "SELECT s.startTime, s.endTime, u.FName AS doctorFName, u.LName AS doctorLName, u.specialty AS doctorSpecialty " +
                     "FROM Schedules s " +
                     "JOIN users_table u ON s.doctorID = u.userID " +
                     "WHERE s.patientID = ? AND s.isBooked = TRUE";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String doctorName = rs.getString("doctorFName") + " " + rs.getString("doctorLName");
                String doctorSpecialty = rs.getString("doctorSpecialty");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                appointments.add(new AppointmentDetails(doctorName, doctorSpecialty, startTime, endTime));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments for patient: " + e.getMessage());
        }
        return appointments;
    }

    public static List<User> searchDoctors(String searchTerm) {
        List<User> doctors = new ArrayList<>();
        String sql = "SELECT userID, FName, LName, specialty, username, userType FROM users_table " +
                     "WHERE userType = 'Doctor' AND " +
                     "(FName LIKE ? OR LName LIKE ? OR specialty LIKE ?)";
        String likeSearchTerm = "%" + searchTerm + "%";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, likeSearchTerm);
            pstmt.setString(2, likeSearchTerm);
            pstmt.setString(3, likeSearchTerm);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("userType"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    // Modified getDoctorAvailability (Patient Booking Process)
    public static List<String> getDoctorAvailability(int doctorId, LocalDate date) {
        List<String> availableSlots = new ArrayList<>();
        final LocalTime startTimeMorning = LocalTime.of(8, 0);
        final LocalTime endTimeMorning = LocalTime.of(12, 0);
        final LocalTime startTimeAfternoon = LocalTime.of(14, 0);
        final LocalTime endTimeAfternoon = LocalTime.of(17, 30);
        final int slotDurationMinutes = 30;

        List<LocalTime> bookedOrUnavailableStartTimes = new ArrayList<>();
        String bookedSql = "SELECT startTime FROM Schedules WHERE doctorID = ? AND date(startTime) = ? AND isBooked = TRUE";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(bookedSql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setString(2, date.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LocalDateTime bookedDateTime = LocalDateTime.parse(rs.getString("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                bookedOrUnavailableStartTimes.add(bookedDateTime.toLocalTime());
            }
        } catch (SQLException e) {
            System.err.println("Error getting booked/unavailable slots: " + e.getMessage());
            return availableSlots; 
        }

        LocalTime currentSlotTime = startTimeMorning;
        while (currentSlotTime.isBefore(endTimeMorning)) {
            if (!bookedOrUnavailableStartTimes.contains(currentSlotTime)) {
                availableSlots.add(String.format("%02d:%02d", currentSlotTime.getHour(), currentSlotTime.getMinute()) +
                                   " - " + String.format("%02d:%02d", currentSlotTime.plusMinutes(slotDurationMinutes).getHour(), currentSlotTime.plusMinutes(slotDurationMinutes).getMinute()));
            }
            currentSlotTime = currentSlotTime.plusMinutes(slotDurationMinutes);
        }

        currentSlotTime = startTimeAfternoon;
        while (currentSlotTime.isBefore(endTimeAfternoon)) {
            if (!bookedOrUnavailableStartTimes.contains(currentSlotTime)) {
                 availableSlots.add(String.format("%02d:%02d", currentSlotTime.getHour(), currentSlotTime.getMinute()) +
                                   " - " + String.format("%02d:%02d", currentSlotTime.plusMinutes(slotDurationMinutes).getHour(), currentSlotTime.plusMinutes(slotDurationMinutes).getMinute()));
            }
            currentSlotTime = currentSlotTime.plusMinutes(slotDurationMinutes);
        }
        return availableSlots;
    }

    public static boolean isDoctorFullyBooked(int doctorId, LocalDate date) {
        return getDoctorAvailability(doctorId, date).isEmpty();
    }

    public static LocalDateTime parseSlotTime(LocalDate date, String timeSlotString) {
        if (date == null || timeSlotString == null || timeSlotString.isEmpty()) {
            return null;
        }
        String startTimeStr = timeSlotString.split(" - ")[0];
        LocalTime localTime = LocalTime.parse(startTimeStr);
        return LocalDateTime.of(date, localTime);
    }

    public static boolean bookAppointment(int doctorId, int patientId, LocalDateTime startTime, LocalDateTime endTime) {
        String checkSql = "SELECT scheduleID FROM Schedules WHERE doctorID = ? AND startTime = ? AND isBooked = TRUE";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, doctorId);
            checkPstmt.setString(2, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ResultSet rs = checkPstmt.executeQuery();
            if (rs.next()) {
                System.err.println("Booking failed: Slot for Dr. " + doctorId + " at " + startTime + " is no longer available.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error checking appointment availability: " + e.getMessage());
            return false;
        }

        String insertSql = "INSERT INTO Schedules (doctorID, patientID, startTime, endTime, isBooked) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
            insertPstmt.setInt(1, doctorId);
            insertPstmt.setInt(2, patientId);
            insertPstmt.setString(3, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            insertPstmt.setString(4, endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            int affectedRows = insertPstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Appointment booked successfully for patient " + patientId + " with Dr. " + doctorId + " from " + startTime + " to " + endTime);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error booking appointment: " + e.getMessage());
        }
        return false;
    }

    public static String getUpcomingAppointmentsForContext(int patientId) {
        StringBuilder contextBuilder = new StringBuilder();
        String sql = "SELECT s.startTime, u.FName AS doctorFName, u.LName AS doctorLName " +
                     "FROM Schedules s " +
                     "JOIN users_table u ON s.doctorID = u.userID " +
                     "WHERE s.patientID = ? AND s.isBooked = TRUE AND date(s.startTime) >= date('now') " +
                     "ORDER BY s.startTime ASC LIMIT 3";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                if (count == 0) {
                    contextBuilder.append("Your upcoming appointments: ");
                } else {
                    contextBuilder.append("; ");
                }
                String doctorName = rs.getString("doctorFName") + " " + rs.getString("doctorLName");
                LocalDateTime st = LocalDateTime.parse(rs.getString("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String formattedStartTime = st.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
                contextBuilder.append("Dr. ").append(doctorName).append(" on ").append(formattedStartTime);
                count++;
            }
            if (count == 0) {
                return "You have no upcoming appointments.";
            }
        } catch (SQLException e) {
            System.err.println("Error getting upcoming appointments for context: " + e.getMessage());
            return "Could not retrieve appointment context due to an error.";
        }
        return contextBuilder.toString();
    }

    public static User getUserById(int userId) {
        String sql = "SELECT userID, username, password, userType, FName, LName, specialty, phoneNumber FROM users_table WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("userType"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getString("specialty"),
                        rs.getString("phoneNumber")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean updateUserProfile(int userId, String firstName, String lastName, String phoneNumber) {
        String sql = "UPDATE users_table SET FName = ?, LName = ?, phoneNumber = ? WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setInt(4, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateUserPassword(int userId, String newHashedPassword) {
        String sql = "UPDATE users_table SET password = ? WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user password: " + e.getMessage());
            return false;
        }
    }

    public static List<DoctorAppointmentDetails> getAppointmentsForDoctor(int doctorId) {
        List<DoctorAppointmentDetails> appointments = new ArrayList<>();
        String sql = "SELECT s.startTime, s.endTime, u.FName AS patientFName, u.LName AS patientLName, u.phoneNumber AS patientPhone " +
                     "FROM Schedules s " +
                     "JOIN users_table u ON s.patientID = u.userID " +
                     "WHERE s.doctorID = ? AND s.isBooked = TRUE AND s.patientID IS NOT NULL " +
                     "ORDER BY s.startTime ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String patientName = rs.getString("patientFName") + " " + rs.getString("patientLName");
                String patientPhone = rs.getString("patientPhone");
                LocalDateTime st = LocalDateTime.parse(rs.getString("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime et = LocalDateTime.parse(rs.getString("endTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String formattedStartTime = st.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                String formattedEndTime = et.format(DateTimeFormatter.ofPattern("HH:mm"));
                appointments.add(new DoctorAppointmentDetails(patientName, patientPhone, formattedStartTime, formattedEndTime));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments for doctor: " + e.getMessage());
        }
        return appointments;
    }

    public static boolean markTimeUnavailable(int doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        String checkPatientBookingSql = "SELECT scheduleID FROM Schedules WHERE doctorID = ? AND patientID IS NOT NULL AND isBooked = TRUE AND " +
                                    " ( (startTime < ?) AND (endTime > ?) )"; // Simplified overlap: new_start < existing_end AND new_end > existing_start

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkPatientBookingSql)) {
            LocalDateTime currentSegmentStart = startTime;
            while(currentSegmentStart.isBefore(endTime)){
                LocalDateTime currentSegmentEnd = currentSegmentStart.plusMinutes(30);
                 if (currentSegmentEnd.isAfter(endTime)) { // Ensure segment does not exceed overall endTime
                    currentSegmentEnd = endTime;
                }
                checkStmt.setInt(1, doctorId);
                checkStmt.setString(2, currentSegmentEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                checkStmt.setString(3, currentSegmentStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.err.println("Cannot mark time unavailable: Overlaps with an existing patient booking for slot starting at " + currentSegmentStart);
                    return false; 
                }
                currentSegmentStart = currentSegmentEnd; // Move to the next segment
                 if (currentSegmentStart.equals(endTime)) break;
            }
        } catch (SQLException e) {
            System.err.println("Error checking for patient booking overlaps: " + e.getMessage());
            return false;
        }

        String insertSql = "INSERT INTO Schedules (doctorID, patientID, startTime, endTime, isBooked) VALUES (?, NULL, ?, ?, TRUE)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            LocalDateTime currentSlotStart = startTime;
            while (currentSlotStart.isBefore(endTime)) {
                LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(30);
                if (currentSlotEnd.isAfter(endTime)) {
                    currentSlotEnd = endTime;
                }
                pstmt.setInt(1, doctorId);
                pstmt.setString(2, currentSlotStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                pstmt.setString(3, currentSlotEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                pstmt.addBatch();
                currentSlotStart = currentSlotEnd;
                 if (currentSlotStart.equals(endTime)) break;
            }
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            System.err.println("Error marking time unavailable: " + e.getMessage());
            return false;
        }
    }
    
    public static List<DoctorScheduleEntry> getDoctorScheduleForDate(int doctorId, LocalDate date) {
        List<DoctorScheduleEntry> scheduleEntries = new ArrayList<>();
        String sql = "SELECT s.startTime, s.endTime, s.patientID, u.FName AS patientFName, u.LName AS patientLName " +
                     "FROM Schedules s " +
                     "LEFT JOIN users_table u ON s.patientID = u.userID " + 
                     "WHERE s.doctorID = ? AND date(s.startTime) = ? AND s.isBooked = TRUE " +
                     "ORDER BY s.startTime ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setString(2, date.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LocalDateTime startTime = LocalDateTime.parse(rs.getString("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime endTime = LocalDateTime.parse(rs.getString("endTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String status;
                String patientName = null;
                if (rs.getObject("patientID") != null) { 
                    status = "Booked by Patient";
                    patientName = rs.getString("patientFName") + " " + rs.getString("patientLName");
                } else {
                    status = "Unavailable";
                }
                scheduleEntries.add(new DoctorScheduleEntry(date, startTime.toLocalTime(), endTime.toLocalTime(), status, patientName));
            }
        } catch (SQLException e) {
            System.err.println("Error getting doctor schedule for date: " + e.getMessage());
        }
        return scheduleEntries;
    }
}
