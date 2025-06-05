package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.User;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

/**
 * Panel for displaying and managing a doctor's schedule.
 * Shows a date picker and a table of appointments for the selected date.
 */
public class DoctorSchedulePanel extends JPanel {

    private final User currentUser;
    private final JTable appointmentTable;

    /**
     * Constructs the DoctorSchedulePanel for the given user.
     * @param user the current user (doctor)
     */
    public DoctorSchedulePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Doctor Schedule for Dr. " + user.getFName());
        add(titleLabel, BorderLayout.NORTH);

        // Date picker for selecting the day
        DatePickerSettings dateSettings = new DatePickerSettings();
        DatePicker datePicker = new DatePicker(dateSettings);
        datePicker.setDateToToday();
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Select Date:"));
        datePanel.add(datePicker);
        add(datePanel, BorderLayout.WEST);

        // Table for appointments
        String[] columnNames = {"Time", "Patient", "Status"};
        Object[][] data = {}; // Will be filled dynamically
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames);
        appointmentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        add(scrollPane, BorderLayout.CENTER);

        // Load today's appointments initially
        loadAppointmentsForDate(datePicker.getDate(), model);

        // Listener for date changes
        datePicker.addDateChangeListener(e -> {
            LocalDate selectedDate = e.getNewDate();
            loadAppointmentsForDate(selectedDate, model);
        });

        // Optionally, add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAppointmentsForDate(datePicker.getDate(), model));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Loads and displays the appointments for the given date in the table.
     * @param date The date to load appointments for.
     * @param model The table model to populate.
     */
    private void loadAppointmentsForDate(LocalDate date, javax.swing.table.DefaultTableModel model) {
        model.setRowCount(0); // Clear table
        if (date == null) return;
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Appointment appt : appointments) {
            if (appt.getAppointmentTime() != null &&
                appt.getAppointmentTime().toLocalDate().equals(date)) {
                String time = appt.getAppointmentTime().toLocalTime().format(timeFormatter);
                String patient = String.valueOf(appt.getPatientID()); // Replace with patient name if available
                String status = appt.getStatus();
                model.addRow(new Object[]{time, patient, status});
            }
        }
    }
}
