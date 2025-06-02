package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class DoctorHomeView extends JFrame {

    private User loggedInDoctor;
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsTableModel;

    public DoctorHomeView(User doctor) {
        this.loggedInDoctor = doctor;
        setTitle("Doctor Dashboard - Dr. " + doctor.lastName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadAppointments();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for doctor info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Logged in as Dr. " + loggedInDoctor.firstName + " " + loggedInDoctor.lastName + " (ID: " + loggedInDoctor.userID + ", Specialty: " + loggedInDoctor.specialty + ")");
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Home Tab (Appointments List)
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        appointmentsPanel.setBorder(BorderFactory.createTitledBorder("My Booked Appointments"));
        appointmentsTableModel = new DefaultTableModel(new String[]{"Patient Name", "Start Time", "End Time", "Patient Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentsTable = new JTable(appointmentsTableModel);
        JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsTable);
        appointmentsPanel.add(appointmentsScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("My Appointments", appointmentsPanel);

        // Schedule Management Tab
        DoctorScheduleManagementView scheduleManagementViewPanel = new DoctorScheduleManagementView(loggedInDoctor);
        tabbedPane.addTab("Manage Schedule", scheduleManagementViewPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void loadAppointments() {
        List<DoctorAppointmentDetails> appointments = DatabaseManager.getAppointmentsForDoctor(loggedInDoctor.userID);
        appointmentsTableModel.setRowCount(0); // Clear existing
        if (appointments != null && !appointments.isEmpty()) {
            for (DoctorAppointmentDetails appt : appointments) {
                Vector<Object> row = new Vector<>();
                row.add(appt.getPatientName());
                row.add(appt.getStartTime());
                row.add(appt.getEndTime());
                row.add(appt.getPatientPhoneNumber() != null ? appt.getPatientPhoneNumber() : "N/A");
                appointmentsTableModel.addRow(row);
            }
        } else {
            System.out.println("No appointments found for Dr. ID: " + loggedInDoctor.userID);
            // Optionally, display a message in the table itself or a label
        }
    }
    
    public void refreshAppointmentsList() {
        loadAppointments();
    }

    private void logout() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new AuthView().setVisible(true));
    }
}

// DTO for Doctor's view of appointments
class DoctorAppointmentDetails {
    private String patientName;
    private String patientPhoneNumber;
    private String startTime;
    private String endTime;

    public DoctorAppointmentDetails(String patientName, String patientPhoneNumber, String startTime, String endTime) {
        this.patientName = patientName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPatientName() { return patientName; }
    public String getPatientPhoneNumber() { return patientPhoneNumber; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    @Override
    public String toString() {
        return "DoctorAppointmentDetails{" +
                "patientName='" + patientName + '\'' +
                ", patientPhoneNumber='" + patientPhoneNumber + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
