package com.example;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class DoctorScheduleManagementView extends JPanel {

    private User loggedInDoctor;
    private JDateChooser dateChooser;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JButton markUnavailableButton;
    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;

    public DoctorScheduleManagementView(User doctor) {
        this.loggedInDoctor = doctor;
        initComponents();
        // Load schedule for today by default or leave empty until date selection
        dateChooser.setDate(new Date()); // Set to today
        loadScheduleForSelectedDate(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Date selection and time marking
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Select Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMinSelectableDate(new Date()); // Doctors can only manage future/present schedules
        controlPanel.add(dateChooser, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        SpinnerDateModel startTimeModel = new SpinnerDateModel();
        startTimeSpinner = new JSpinner(startTimeModel);
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startTimeEditor);
        setSpinnerTimeTo8AM(startTimeSpinner); // Default to 8 AM
        controlPanel.add(startTimeSpinner, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        controlPanel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1;
        SpinnerDateModel endTimeModel = new SpinnerDateModel();
        endTimeSpinner = new JSpinner(endTimeModel);
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        setSpinnerTimeTo5PM(endTimeSpinner); // Default to 5 PM
        controlPanel.add(endTimeSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        markUnavailableButton = new JButton("Mark Selected Time as Unavailable");
        controlPanel.add(markUnavailableButton, gbc);

        add(controlPanel, BorderLayout.NORTH);

        // Center Panel: Schedule Display Table
        scheduleTableModel = new DefaultTableModel(new String[]{"Date", "Start Time", "End Time", "Status", "Patient"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        scheduleTable = new JTable(scheduleTableModel);
        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);

        // Action Listeners
        dateChooser.addPropertyChangeListener("date", evt -> {
            if ("date".equals(evt.getPropertyName()) && dateChooser.getDate() != null) {
                loadScheduleForSelectedDate();
            }
        });
        markUnavailableButton.addActionListener(e -> markTimeAsUnavailableAction());
    }
    
    private void setSpinnerTimeTo8AM(JSpinner spinner) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        spinner.setValue(calendar.getTime());
    }

    private void setSpinnerTimeTo5PM(JSpinner spinner) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        spinner.setValue(calendar.getTime());
    }


    private void loadScheduleForSelectedDate() {
        scheduleTableModel.setRowCount(0); // Clear table
        if (dateChooser.getDate() == null) return;

        LocalDate selectedDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<DoctorScheduleEntry> scheduleEntries = DatabaseManager.getDoctorScheduleForDate(loggedInDoctor.userID, selectedDate);

        if (scheduleEntries != null && !scheduleEntries.isEmpty()) {
            for (DoctorScheduleEntry entry : scheduleEntries) {
                Vector<Object> row = new Vector<>();
                row.add(entry.getDate().toString());
                row.add(entry.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                row.add(entry.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                row.add(entry.getStatus());
                row.add(entry.getPatientName() != null ? entry.getPatientName() : "N/A");
                scheduleTableModel.addRow(row);
            }
        } else {
            // Optionally display "No schedule entries for this date"
             scheduleTableModel.addRow(new Object[]{selectedDate.toString(), "-", "-", "No entries", "N/A"});
        }
    }

    private void markTimeAsUnavailableAction() {
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate selectedDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        Date selectedStartTimeDate = (Date) startTimeSpinner.getValue();
        Date selectedEndTimeDate = (Date) endTimeSpinner.getValue();

        LocalTime selectedLocalStartTime = selectedStartTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime selectedLocalEndTime = selectedEndTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

        if (selectedLocalStartTime.equals(selectedLocalEndTime) || selectedLocalStartTime.isAfter(selectedLocalEndTime)) {
            JOptionPane.showMessageDialog(this, "End time must be after start time.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(selectedDate, selectedLocalStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(selectedDate, selectedLocalEndTime);

        boolean success = DatabaseManager.markTimeUnavailable(loggedInDoctor.userID, startDateTime, endDateTime);
        if (success) {
            JOptionPane.showMessageDialog(this, "Time marked as unavailable successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadScheduleForSelectedDate(); // Refresh the schedule display
        } else {
            JOptionPane.showMessageDialog(this, "Failed to mark time as unavailable. It might overlap with an existing patient booking or another error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// DTO for displaying entries in the doctor's schedule management view
class DoctorScheduleEntry {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // "Booked by Patient", "Unavailable"
    private String patientName; // Null if unavailable

    public DoctorScheduleEntry(LocalDate date, LocalTime startTime, LocalTime endTime, String status, String patientName) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.patientName = patientName;
    }

    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public String getPatientName() { return patientName; }
}
