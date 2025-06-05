package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.User;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;

public class PatientBookingPanel extends JPanel {

    private User currentUser;
    private DatePicker datePicker;
    private JComboBox<String> timePicker;
    private Doctor selectedDoctor;

    // Custom renderer for the doctor list
    private static class DoctorListRenderer extends DefaultListCellRenderer {
        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            switch (value) {
                case Doctor doctor -> {
                    setText(doctor.getFName() + " " + doctor.getLName() + " (" + doctor.getSpecialty() + ")");
                }
                case String text -> {
                    setText(text);
                }
                default -> {
                    // Default rendering for other types
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }
            return this;
        }
    }

    private static class TodayHighlightPolicy implements DateHighlightPolicy {
        @Override
        public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
            if (date != null && date.isEqual(LocalDate.now())) {
                return new HighlightInformation(Color.ORANGE, null, "Today");
            }
            return null;
        }
    }

    private static class OccupiedDateVetoPolicy implements DateVetoPolicy {
        private final List<Appointment> appointments;

        public OccupiedDateVetoPolicy(List<Appointment> appointments) {
            this.appointments = appointments;
        }

        @Override
        public boolean isDateAllowed(LocalDate date) {
            if (date == null) {
                return true;
            }
            if (date.isBefore(LocalDate.now())) {
                return false;
            }
            for (Appointment appt : appointments) {
                if (appt.getAppointmentTime() != null) {
                    LocalDate apptDate = LocalDate.of(
                        appt.getAppointmentTime().getYear(),
                        appt.getAppointmentTime().getMonthValue(),
                        appt.getAppointmentTime().getDayOfMonth()
                    );
                    if (apptDate.equals(date)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public PatientBookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("Book Your Appointment"), BorderLayout.NORTH);

        // Doctor search panel
        JPanel doctorSearchPanel = new JPanel();
        doctorSearchPanel.setLayout(new BoxLayout(doctorSearchPanel, BoxLayout.Y_AXIS));
        doctorSearchPanel.setBorder(BorderFactory.createTitledBorder("Find a Doctor"));

        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchInputPanel.add(new JLabel("Name or Specialty:"));
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);

        doctorSearchPanel.add(searchInputPanel);
        doctorSearchPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        DefaultListModel<Object> doctorListModel = new DefaultListModel<>();
        JList<Object> doctorList = new JList<>(doctorListModel);
        doctorList.setCellRenderer(new DoctorListRenderer());
        JScrollPane doctorListScrollPane = new JScrollPane(doctorList);
        doctorListScrollPane.setPreferredSize(new Dimension(400, 100));
        doctorSearchPanel.add(doctorListScrollPane);

        // Booking controls panel
        JPanel bookingControlsPanel = new JPanel();
        bookingControlsPanel.setLayout(new BoxLayout(bookingControlsPanel, BoxLayout.X_AXIS));

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setHighlightPolicy(new TodayHighlightPolicy());

        // Fetch existing appointments for the current user
        List<Appointment> userAppointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        dateSettings.setVetoPolicy(new OccupiedDateVetoPolicy(userAppointments));
        datePicker = new DatePicker(dateSettings);
        datePicker.setDateToToday();

        timePicker = new JComboBox<>();
        timePicker.addItem("Select a doctor first");

        bookingControlsPanel.add(new JLabel(" Select Date: "));
        bookingControlsPanel.add(datePicker);
        bookingControlsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bookingControlsPanel.add(new JLabel("Select Time: "));
        bookingControlsPanel.add(timePicker);

        // Center content panel
        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.add(doctorSearchPanel);
        centerContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerContentPanel.add(bookingControlsPanel);

        add(centerContentPanel, BorderLayout.CENTER);

        // Doctor search button action
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            doctorListModel.clear();
            List<Doctor> uniqueDoctors = DatabaseAccessor.getDoctorsBySpecialty(query);
            if (!uniqueDoctors.isEmpty()) {
                for (Doctor doctor : uniqueDoctors) {
                    doctorListModel.addElement(doctor);
                }
            } else {
                doctorListModel.addElement("No doctors found");
            }
        });

        // Doctor list selection listener
        doctorList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object selectedValue = doctorList.getSelectedValue();
                if (selectedValue instanceof Doctor doctor) {
                    this.selectedDoctor = doctor;
                    LocalDate selectedDate = datePicker.getDate();
                    if (selectedDate != null) {
                        populateTimePicker(timePicker, selectedDate, this.selectedDoctor);
                    } else {
                        timePicker.removeAllItems();
                        timePicker.addItem("Select a date");
                    }
                } else {
                    this.selectedDoctor = null;
                    timePicker.removeAllItems();
                    timePicker.addItem("Select a doctor");
                }
            }
        });

        // Date picker change listener
        datePicker.addDateChangeListener(event -> {
            LocalDate selectedDate = event.getNewDate();
            if (selectedDate != null && this.selectedDoctor != null) {
                populateTimePicker(timePicker, selectedDate, this.selectedDoctor);
            } else if (selectedDate != null && this.selectedDoctor == null) {
                timePicker.removeAllItems();
                timePicker.addItem("Select a doctor first");
            } else {
                timePicker.removeAllItems();
            }
        });

        // Booking confirmation button
        JButton bookingButton = new JButton("Book Appointment");
        add(bookingButton, BorderLayout.SOUTH);
        bookingButton.addActionListener(e -> {
            LocalDate selectedDate = datePicker.getDate();
            String selectedTimeSlot = (String) timePicker.getSelectedItem();

            if (selectedDate == null || selectedTimeSlot == null || selectedTimeSlot.startsWith("No slots")) {
                JOptionPane.showMessageDialog(this, "Please select a valid date and time.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.selectedDoctor == null) {
                JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String startTimeString = selectedTimeSlot.split(" - ")[0];
                java.time.LocalTime selectedTime = java.time.LocalTime.parse(startTimeString);
                LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, selectedTime);
                int doctorId = this.selectedDoctor.getUserID();
                Appointment newAppointment = new Appointment(currentUser.getUserID(), doctorId, appointmentDateTime, "Scheduled");
                // Add the appointment to the database
                DatabaseAccessor.addAppointment(newAppointment);
                JOptionPane.showMessageDialog(this, "Appointment booked for " + selectedDate + " at " + startTimeString);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Helper method to get all available time slots for a day
    public List<String> getAllTimeSlots() {
        List<String> allSlots = new java.util.ArrayList<>();
        for (int hour = 8; hour < 17; hour++) {
            allSlots.add(String.format("%02d:00 - %02d:30", hour, hour));
            if (hour < 16) {
                allSlots.add(String.format("%02d:30 - %02d:00", hour, hour + 1));
            }
        }
        return allSlots;
    }

    // Helper method to populate the time picker for a doctor and date
    private void populateTimePicker(JComboBox<String> timePickerComboBox, LocalDate selectedDate, Doctor doctor) {
        timePickerComboBox.removeAllItems();
        if (doctor == null) {
            timePickerComboBox.addItem("Select a doctor");
            return;
        }
        if (selectedDate == null) {
            timePickerComboBox.addItem("Select a date");
            return;
        }
        List<Appointment> doctorAppointments = DatabaseAccessor.getAppointments(doctor.getUserID());
        List<LocalDateTime> occupiedAppointments = new java.util.ArrayList<>();
        if (doctorAppointments != null) {
            for (Appointment appt : doctorAppointments) {
            if (appt.getAppointmentTime() != null) {
                occupiedAppointments.add(appt.getAppointmentTime());
            }
            }
        }
        List<String> allSlots = getAllTimeSlots();
        boolean slotsAdded = false;
        for (String slot : allSlots) {
            String startTimeString = slot.split(" - ")[0];
            java.time.LocalTime slotStartTime = java.time.LocalTime.parse(startTimeString);
            // LocalDateTime slotDateTime = LocalDateTime.of(selectedDate, slotStartTime);
            boolean isOccupied = false;
                for (LocalDateTime occupiedTime : occupiedAppointments) {
                    if (occupiedTime.toLocalTime().equals(slotStartTime)) {
                        isOccupied = true;
                        break;
                    }
                
            }
            if (!isOccupied) {
                timePickerComboBox.addItem(slot);
                slotsAdded = true;
            }
        }
        if (!slotsAdded) {
            timePickerComboBox.addItem("No slots available for this date and doctor");
        }
    }
}