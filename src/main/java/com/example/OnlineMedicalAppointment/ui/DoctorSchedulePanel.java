package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

    // Define some basic high-contrast colors (adjust as needed)
    private static final Color PRIMARY_BACKGROUND = new Color(240, 240, 240); // Light gray
    private static final Color SECONDARY_BACKGROUND = new Color(220, 220, 220); // Slightly darker gray
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color HEADER_BACKGROUND = new Color(70, 130, 180); // Steel blue
    private static final Color HEADER_FOREGROUND = Color.WHITE;
    private static final Color BUTTON_BACKGROUND = new Color(50, 150, 200); // Medium blue
    private static final Color BUTTON_FOREGROUND = Color.WHITE;
    private static final Color ERROR_COLOR = Color.RED;
    private static final Color STATUS_COLOR = new Color(0, 100, 0); // Dark green

    /**
     * Constructs the DoctorSchedulePanel for the given user.
     * @param user the current user (doctor)
     */
    public DoctorSchedulePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(PRIMARY_BACKGROUND); // Set panel background

        // Create and configure components
        JLabel titleLabel = new JLabel("Appointment Schedule - Dr. " + user.getFName() + " " + user.getLName());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f)); // Increased font size
        titleLabel.setForeground(TEXT_COLOR); // Set text color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.setBackground(SECONDARY_BACKGROUND); // Set panel background
        datePanel.setBorder(BorderFactory.createTitledBorder("Select Date")); // Add border

        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.getJCalendar().setWeekOfYearVisible(false);
        dateChooser.setPreferredSize(new Dimension(150, 28));
        dateChooser.setFont(dateChooser.getFont().deriveFont(14f));
        dateChooser.setForeground(TEXT_COLOR); // Set text color
        // JDateChooser uses internal components, styling might be limited here.

        JButton todayButton = new JButton("Today");
        todayButton.setBackground(BUTTON_BACKGROUND); // Set button background
        todayButton.setForeground(BUTTON_FOREGROUND); // Set button text color
        todayButton.setFocusPainted(false); // Improve button appearance
        todayButton.addActionListener(e -> {
            dateChooser.setDate(new Date());
            loadAppointmentsForSelectedDate();
        });

        JLabel selectDateLabel = new JLabel("Select Date:");
        selectDateLabel.setForeground(TEXT_COLOR); // Set text color
        selectDateLabel.setFont(selectDateLabel.getFont().deriveFont(14f));

        datePanel.add(selectDateLabel);
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
        appointmentTable.setBackground(Color.WHITE); // Table background
        appointmentTable.setForeground(TEXT_COLOR); // Table text color
        appointmentTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        appointmentTable.setSelectionForeground(TEXT_COLOR); // Selection text color
        appointmentTable.setFont(appointmentTable.getFont().deriveFont(14f));

        // Table header styling
        JTableHeader tableHeader = appointmentTable.getTableHeader();
        tableHeader.setBackground(HEADER_BACKGROUND);
        tableHeader.setForeground(HEADER_FOREGROUND);
        tableHeader.setFont(tableHeader.getFont().deriveFont(Font.BOLD, 14f));
        tableHeader.setReorderingAllowed(false); // Prevent column reordering

        // Set column widths
        TableColumn column;
        for (int i = 0; i < appointmentTable.getColumnCount(); i++) {
            column = appointmentTable.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(80); // Time
                    break;
                case 1:
                    column.setPreferredWidth(200); // Patient Name
                    break;
                default:
                    column.setPreferredWidth(100); // Status
                    break;
            }
        }

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(SECONDARY_BACKGROUND, 1)); // Add border

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(PRIMARY_BACKGROUND); // Set panel background

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(BUTTON_BACKGROUND); // Set button background
        refreshButton.setForeground(BUTTON_FOREGROUND); // Set button text color
        refreshButton.setFocusPainted(false); // Improve button appearance
        java.net.URL iconUrl = getClass().getResource("/icons/refresh.png"); // Ensure icon has good contrast
        if (iconUrl != null) {
            refreshButton.setIcon(new ImageIcon(iconUrl));
        }
        refreshButton.addActionListener(e -> loadAppointmentsForSelectedDate());

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(TEXT_COLOR); // Set text color
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 12f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        buttonPanel.add(statusLabel);
        buttonPanel.add(refreshButton);

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        // Use a panel to hold datePanel and scrollPane in the center area
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(PRIMARY_BACKGROUND);
        centerPanel.add(datePanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
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
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setText("Loading appointments...");

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
            statusLabel.setForeground(TEXT_COLOR);
            statusLabel.setText("No appointments found for " + date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
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

        statusLabel.setForeground(STATUS_COLOR); // Indicate success/info
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
        // Capitalize first letter
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
        statusLabel.setForeground(ERROR_COLOR); // Indicate error
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
        // Disable/enable buttons if needed during loading
        // refreshButton.setEnabled(!loading);
        // todayButton.setEnabled(!loading);
        // dateChooser.setEnabled(!loading);
    }
}