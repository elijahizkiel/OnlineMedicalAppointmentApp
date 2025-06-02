package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class PatientHomeView extends JFrame {

    private User loggedInUser;
    private JTable doctorsTable;
    private JTable appointmentsTable;
    private DefaultTableModel doctorsTableModel;
    private DefaultTableModel appointmentsTableModel;

    public PatientHomeView(User user) {
        this.loggedInUser = user;
        setTitle("Patient Dashboard - Welcome " + user.firstName + " " + user.lastName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadDoctors();
        loadAppointments();
    }

    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for user info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Patient ID: " + loggedInUser.userID + " (" + loggedInUser.userType + ")");
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Dashboard Panel (existing tables)
        JPanel dashboardPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Doctors List Panel
        JPanel doctorsPanel = new JPanel(new BorderLayout());
        doctorsPanel.setBorder(BorderFactory.createTitledBorder("Available Doctors"));
        doctorsTableModel = new DefaultTableModel(new String[]{"Name", "Specialty", "Doctor ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        doctorsTable = new JTable(doctorsTableModel);
        doctorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane doctorsScrollPane = new JScrollPane(doctorsTable);
        doctorsPanel.add(doctorsScrollPane, BorderLayout.CENTER);
        dashboardPanel.add(doctorsPanel);

        // Appointments List Panel
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        appointmentsPanel.setBorder(BorderFactory.createTitledBorder("My Appointments"));
        appointmentsTableModel = new DefaultTableModel(new String[]{"Doctor Name", "Specialty", "Start Time", "End Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentsTable = new JTable(appointmentsTableModel);
        JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsTable);
        appointmentsPanel.add(appointmentsScrollPane, BorderLayout.CENTER);
        dashboardPanel.add(appointmentsPanel);
        
        tabbedPane.addTab("Dashboard", dashboardPanel);

        // Booking Panel
        BookingView bookingViewPanel = new BookingView(loggedInUser, this); // Pass 'this'
        tabbedPane.addTab("Book Appointment", bookingViewPanel);
        
        // Chat Panel
        ChatRoomView chatRoomViewPanel = new ChatRoomView(loggedInUser);
        tabbedPane.addTab("Chat Room", chatRoomViewPanel);

        // Profile Panel
        ProfileView profileViewPanel = new ProfileView(loggedInUser, this);
        tabbedPane.addTab("Profile", profileViewPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void loadDoctors() {
        List<User> doctors = DatabaseManager.getAllDoctors();
        doctorsTableModel.setRowCount(0); // Clear existing data
        if (doctors != null && !doctors.isEmpty()) {
            for (User doctor : doctors) {
                Vector<Object> row = new Vector<>();
                row.add(doctor.firstName + " " + doctor.lastName);
                row.add(doctor.specialty);
                row.add(doctor.userID); // Adding doctor ID for potential future use (e.g. booking)
                doctorsTableModel.addRow(row);
            }
        } else {
            // Optionally, display a message in the table or a label
            System.out.println("No doctors found or error loading doctors.");
        }
    }

    private void loadAppointments() {
        List<AppointmentDetails> appointments = DatabaseManager.getAppointmentsForPatient(loggedInUser.userID);
        appointmentsTableModel.setRowCount(0); // Clear existing data
        if (appointments != null && !appointments.isEmpty()) {
            for (AppointmentDetails appointment : appointments) {
                Vector<Object> row = new Vector<>();
                row.add(appointment.getDoctorName());
                row.add(appointment.getDoctorSpecialty());
                row.add(appointment.getStartTime());
                row.add(appointment.getEndTime());
                appointmentsTableModel.addRow(row);
            }
        } else {
            // Optionally, display a message in the table or a label
            System.out.println("No appointments found for patient ID: " + loggedInUser.userID);
        }
    }

    private void logout() {
        // For now, just close this view and show AuthView again
        this.dispose();
        SwingUtilities.invokeLater(() -> new AuthView().setVisible(true));
    }
    
    public void refreshAppointmentsList() {
        System.out.println("Refreshing appointments list for patient ID: " + loggedInUser.userID);
        List<AppointmentDetails> appointments = DatabaseManager.getAppointmentsForPatient(loggedInUser.userID);
        if (appointmentsTableModel == null) { // Ensure model is initialized
            appointmentsTableModel = (DefaultTableModel) appointmentsTable.getModel();
        }
        appointmentsTableModel.setRowCount(0); // Clear existing data
        if (appointments != null && !appointments.isEmpty()) {
            for (AppointmentDetails appointment : appointments) {
                Vector<Object> row = new Vector<>();
                row.add(appointment.getDoctorName());
                row.add(appointment.getDoctorSpecialty());
                row.add(appointment.getStartTime());
                row.add(appointment.getEndTime());
                appointmentsTableModel.addRow(row);
            }
        } else {
            System.out.println("No appointments found for patient ID: " + loggedInUser.userID + " after refresh.");
        }
    }

    public void updateLoggedInUser(User updatedUser) {
        this.loggedInUser = updatedUser;
        // Update any UI elements that display user information, e.g., the welcome label
        setTitle("Patient Dashboard - Welcome " + loggedInUser.firstName + " " + loggedInUser.lastName);
        // Assuming welcomeLabel is accessible or make it so
        // welcomeLabel.setText("Patient ID: " + loggedInUser.userID + " (" + loggedInUser.userType + ")"); 
        System.out.println("PatientHomeView: User details updated to: " + loggedInUser.toString());
    }
}

// A simple DTO for appointment details to be displayed
class AppointmentDetails {
    private String doctorName;
    private String doctorSpecialty;
    private String startTime;
    private String endTime;

    public AppointmentDetails(String doctorName, String doctorSpecialty, String startTime, String endTime) {
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDoctorName() { return doctorName; }
    public String getDoctorSpecialty() { return doctorSpecialty; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "doctorName='" + doctorName + '\'' +
                ", doctorSpecialty='" + doctorSpecialty + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
