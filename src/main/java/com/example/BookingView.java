package com.example;

import com.toedter.calendar.JDateChooser; // Corrected import
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime; // Added import
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class BookingView extends JPanel { // Changed to JPanel

    private User loggedInPatient;
    private JTextField searchDoctorField;
    private JButton searchDoctorButton;
    private JTable doctorsSearchResultTable;
    private DefaultTableModel doctorsSearchResultTableModel;
    private JDateChooser dateChooser; // Using JDateChooser from JCalendar
    private JTable availableSlotsTable;
    private DefaultTableModel availableSlotsTableModel;
    private JButton bookAppointmentButton;

    private User selectedDoctor = null;
    private PatientHomeView patientHomeView; // Reference to PatientHomeView

    public BookingView(User patient, PatientHomeView patientHomeView) { // Modified constructor
        this.loggedInPatient = patient;
        this.patientHomeView = patientHomeView; // Store reference
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Doctor Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchDoctorField = new JTextField(20);
        searchDoctorButton = new JButton("Search Doctors");
        searchPanel.add(new JLabel("Search by Name/Specialty:"));
        searchPanel.add(searchDoctorField);
        searchPanel.add(searchDoctorButton);
        add(searchPanel, BorderLayout.NORTH);

        // Center: Split pane for search results and booking details
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        centerSplitPane.setResizeWeight(0.4); // Give more space to booking details initially

        // Center-Top: Doctor Search Results
        JPanel doctorsResultPanel = new JPanel(new BorderLayout());
        doctorsResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        doctorsSearchResultTableModel = new DefaultTableModel(new String[]{"Name", "Specialty", "Doctor ID"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        doctorsSearchResultTable = new JTable(doctorsSearchResultTableModel);
        doctorsSearchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorsResultPanel.add(new JScrollPane(doctorsSearchResultTable), BorderLayout.CENTER);
        centerSplitPane.setTopComponent(doctorsResultPanel);

        // Center-Bottom: Date, Time Slots, Book button
        JPanel bookingDetailsPanel = new JPanel(new BorderLayout(10,10));
        bookingDetailsPanel.setBorder(BorderFactory.createTitledBorder("Select Date and Time"));

        // Date Chooser
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        // Configure date range (next 3 months, no past dates)
        Calendar today = Calendar.getInstance();
        dateChooser.setMinSelectableDate(today.getTime());
        Calendar threeMonthsLater = Calendar.getInstance();
        threeMonthsLater.add(Calendar.MONTH, 3);
        dateChooser.setMaxSelectableDate(threeMonthsLater.getTime());
        dateChooser.setEnabled(false); // Initially disabled
        datePanel.add(new JLabel("Select Date:"));
        datePanel.add(dateChooser);
        bookingDetailsPanel.add(datePanel, BorderLayout.NORTH);

        // Available Time Slots
        availableSlotsTableModel = new DefaultTableModel(new String[]{"Available Time Slots"}, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        availableSlotsTable = new JTable(availableSlotsTableModel);
        availableSlotsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingDetailsPanel.add(new JScrollPane(availableSlotsTable), BorderLayout.CENTER);

        // Book button
        bookAppointmentButton = new JButton("Book Appointment (Placeholder)");
        bookAppointmentButton.setEnabled(false); // Initially disabled
        bookingDetailsPanel.add(bookAppointmentButton, BorderLayout.SOUTH);
        
        centerSplitPane.setBottomComponent(bookingDetailsPanel);
        add(centerSplitPane, BorderLayout.CENTER);

        // Action Listeners
        searchDoctorButton.addActionListener(e -> searchDoctorsAction());
        doctorsSearchResultTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && doctorsSearchResultTable.getSelectedRow() != -1) {
                int selectedRow = doctorsSearchResultTable.getSelectedRow();
                String doctorName = (String) doctorsSearchResultTableModel.getValueAt(selectedRow, 0);
                String specialty = (String) doctorsSearchResultTableModel.getValueAt(selectedRow, 1);
                int doctorId = (Integer) doctorsSearchResultTableModel.getValueAt(selectedRow, 2);
                selectedDoctor = new User(doctorId, "", "Doctor", doctorName.split(" ")[0], doctorName.split(" ").length > 1 ? doctorName.split(" ")[1] : "", specialty);
                dateChooser.setEnabled(true);
                dateChooser.setDate(null); // Reset date selection
                availableSlotsTableModel.setRowCount(0); // Clear previous slots
                bookAppointmentButton.setEnabled(false);
                 System.out.println("Selected Doctor: ID " + selectedDoctor.userID);
            }
        });

        dateChooser.addPropertyChangeListener("date", evt -> {
            if (selectedDoctor != null && dateChooser.getDate() != null) {
                LocalDate selectedDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                loadAvailableSlots(selectedDoctor.userID, selectedDate);
            }
        });
        
        availableSlotsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && availableSlotsTable.getSelectedRow() != -1 && selectedDoctor != null && dateChooser.getDate() != null) {
                bookAppointmentButton.setEnabled(true); // Enable book button when a slot is selected
            } else {
                bookAppointmentButton.setEnabled(false);
            }
        });

        bookAppointmentButton.addActionListener(e -> bookAppointmentAction());
    }

    private void searchDoctorsAction() {
        String searchTerm = searchDoctorField.getText();
        List<User> doctors = DatabaseManager.searchDoctors(searchTerm);
        doctorsSearchResultTableModel.setRowCount(0); // Clear previous results
        if (doctors != null && !doctors.isEmpty()) {
            for (User doctor : doctors) {
                Vector<Object> row = new Vector<>();
                row.add(doctor.firstName + " " + doctor.lastName);
                row.add(doctor.specialty);
                row.add(doctor.userID);
                doctorsSearchResultTableModel.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No doctors found matching your search.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
        // Reset selections and dependent UI parts
        selectedDoctor = null;
        dateChooser.setDate(null);
        dateChooser.setEnabled(false);
        availableSlotsTableModel.setRowCount(0);
        bookAppointmentButton.setEnabled(false);
    }

    private void loadAvailableSlots(int doctorId, LocalDate date) {
        List<String> slots = DatabaseManager.getDoctorAvailability(doctorId, date);
        availableSlotsTableModel.setRowCount(0); // Clear previous slots
        if (slots != null && !slots.isEmpty()) {
            for (String slot : slots) {
                availableSlotsTableModel.addRow(new Object[]{slot});
            }
        } else {
            availableSlotsTableModel.addRow(new Object[]{"No slots available on this date."});
        }
        bookAppointmentButton.setEnabled(false); // Reset book button state
        
        // Check if doctor is fully booked to potentially update calendar (visual cue)
        // This part is complex for JDateChooser. For now, we just show no slots.
        // if (DatabaseManager.isDoctorFullyBooked(doctorId, date)) {
        //     System.out.println("Dr. " + doctorId + " is fully booked on " + date);
        //     // Code to visually mark date on calendar would go here if using a more advanced calendar
        // }
    }

    private void bookAppointmentAction() {
        if (selectedDoctor == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int selectedSlotRow = availableSlotsTable.getSelectedRow();
        if (selectedSlotRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an available time slot.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String slotString = (String) availableSlotsTableModel.getValueAt(selectedSlotRow, 0);
        if (slotString.contains("No slots available")) {
             JOptionPane.showMessageDialog(this, "The selected slot is not valid for booking.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate localDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startTime = DatabaseManager.parseSlotTime(localDate, slotString);
        if (startTime == null) {
            JOptionPane.showMessageDialog(this, "Invalid time slot format.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDateTime endTime = startTime.plusMinutes(30); // Assuming 30-minute slots

        boolean success = DatabaseManager.bookAppointment(selectedDoctor.userID, loggedInPatient.userID, startTime, endTime);

        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment booked successfully!", "Booking Success", JOptionPane.INFORMATION_MESSAGE);
            // Refresh available slots for the current view
            loadAvailableSlots(selectedDoctor.userID, localDate);
            // Refresh "My Appointments" list in PatientHomeView
            if (patientHomeView != null) {
                patientHomeView.refreshAppointmentsList();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to book appointment. The slot may have just been taken or another error occurred.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
            // Refresh available slots as it might have changed
            loadAvailableSlots(selectedDoctor.userID, localDate);
        }
    }
}
