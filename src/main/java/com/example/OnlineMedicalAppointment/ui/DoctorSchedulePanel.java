package com.example.OnlineMedicalAppointment.ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.User;
import com.toedter.calendar.JDateChooser;

/**
 * Panel for displaying and managing a doctor's schedule.
 * Shows a date picker and a table of appointments for the selected date.
 */
public class DoctorSchedulePanel extends JPanel {

    private final User currentUser;
    private final JTable appointmentTable;
    private final DefaultTableModel tableModel;
    private final JLabel statusLabel;
    private final JDateChooser dateChooser;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs the DoctorSchedulePanel for the given user.
     * @param user the current user (doctor)
     */
    public DoctorSchedulePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        // Create and configure components
        JLabel titleLabel = new JLabel("Appointment Schedule - Dr. " + user.getFName() + " " + user.getLName());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.getJCalendar().setWeekOfYearVisible(false);
        dateChooser.setPreferredSize(new Dimension(150, 28));
        dateChooser.setFont(dateChooser.getFont().deriveFont(14f));

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(e -> {
            dateChooser.setDate(new Date());
            loadAppointmentsForSelectedDate();
        });

        datePanel.add(new JLabel("Select Date:"));
        datePanel.add(dateChooser);
        datePanel.add(todayButton);

        // Table setup
        String[] columnNames = {"Time", "Patient Name", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFillsViewportHeight(true);
        appointmentTable.setRowHeight(25);
        appointmentTable.setAutoCreateRowSorter(true);
        
        // Set column widths
        TableColumn column = null;
        for (int i = 0; i < appointmentTable.getColumnCount(); i++) {
            column = appointmentTable.getColumnModel().getColumn(i);
            if (i == 0) column.setPreferredWidth(80);  // Time
            else if (i == 1) column.setPreferredWidth(200); // Patient Name
            else column.setPreferredWidth(100); // Status
        }

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/icons/refresh.png")));
        refreshButton.addActionListener(e -> loadAppointmentsForSelectedDate());

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        buttonPanel.add(statusLabel);
        buttonPanel.add(refreshButton);

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(datePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial load
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                loadAppointmentsForSelectedDate();
            }
        });
        
        // Load initial data
        loadAppointmentsForSelectedDate();
    }

    /**
     * Loads appointments for the currently selected date
     */
    private void loadAppointmentsForSelectedDate() {
        if (dateChooser.getDate() == null) return;
        
        LocalDate selectedDate = dateChooser.getDate().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
            
        // Show loading state
        setLoading(true);
        
        // Use SwingWorker for database operations to keep UI responsive
        new SwingWorker<List<Appointment>, Void>() {
            @Override
            protected List<Appointment> doInBackground() throws Exception {
                return DatabaseAccessor.getAppointmentsForDoctorOnDate(
                    currentUser.getUserID(), 
                    selectedDate
                );
            }
            
            @Override
            protected void done() {
                try {
                    List<Appointment> appointments = get();
                    updateAppointmentTable(appointments, selectedDate);
                } catch (InterruptedException | ExecutionException e) {
                    showError("Error loading appointments: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    setLoading(false);
                }
            }
        }.execute();
    }
    
    /**
     * Updates the appointment table with the given list of appointments
     */
    private void updateAppointmentTable(List<Appointment> appointments, LocalDate date) {
        tableModel.setRowCount(0);
        
        if (appointments == null || appointments.isEmpty()) {
            statusLabel.setText("No appointments found for " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            return;
        }
        
        // Sort appointments by time
        appointments.sort((a1, a2) -> a1.getAppointmentTime().compareTo(a2.getAppointmentTime()));
        
        // Add appointments to table
        for (Appointment appt : appointments) {
            String time = appt.getAppointmentTime().toLocalTime().format(TIME_FORMATTER);
            String patientName = DatabaseAccessor.getPatientName(appt.getPatientID());
            String status = appt.getStatus();
            
            tableModel.addRow(new Object[]{
                time,
                patientName != null ? patientName : "Unknown Patient",
                formatStatus(status)
            });
        }
        
        statusLabel.setText(String.format("Showing %d appointment(s) for %s", 
            appointments.size(), 
            date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
        ));
    }
    
    /**
     * Formats the status string for display
     */
    private String formatStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "Scheduled";
        }
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }
    
    /**
     * Shows an error message to the user
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
        statusLabel.setText("Error: " + message);
    }
    
    /**
     * Sets the loading state of the panel
     */
    private void setLoading(boolean loading) {
        setCursor(loading ? 
            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : 
            Cursor.getDefaultCursor()
        );
    }
}