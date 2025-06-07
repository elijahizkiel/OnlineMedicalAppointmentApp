package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List; // Import Font

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for booking appointments as a patient.
 * Allows searching for doctors, selecting a date and time, and booking an appointment.
 */
public class PatientBookingPanel extends JPanel {

    /**
     * The current user (patient) using the panel.
     */
    private User currentUser;

    /**
     * Spinner for selecting the appointment date.
     */
    private JSpinner dateSpinner;

    /**
     * Combo box for selecting the appointment time.
     */
    private JComboBox<String> timePicker;

    /**
     * The selected doctor for the appointment.
     */
    private Doctor selectedDoctor;

    /**
     * Editor for the date spinner.
     */
    private JSpinner.DateEditor dateEditor;

    /**
     * Custom renderer for the doctor list.
     */
    private static class DoctorListRenderer extends DefaultListCellRenderer {
        /**
         * Returns a component that has been configured to display the specified value.
         * @param list the JList we're painting
         * @param value the value returned by list.getModel().getElementAt(index)
         * @param index the cell's index
         * @param isSelected true if the specified cell was selected
         * @param cellHasFocus true if the specified cell has the focus
         * @return the component used to render the value
         */
        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            // Assuming StyleConstants.NORMAL_FONT provides a readable font
            setFont(StyleConstants.NORMAL_FONT);

            if (value instanceof Doctor doctor) {
                setText(doctor.getFName() + " " + doctor.getLName() + " (" + doctor.getSpecialty() + ")");
                // Assuming StyleConstants colors provide good contrast
                if (isSelected) {
                    setBackground(StyleConstants.PRIMARY_COLOR.darker());
                    setForeground(StyleConstants.WHITE);
                } else {
                    setBackground(StyleConstants.WHITE);
                    setForeground(StyleConstants.TEXT_COLOR);
                }
            } else if (value instanceof String text) {
                setText(text);
                // Assuming StyleConstants fonts and colors provide good contrast
                setFont(StyleConstants.NORMAL_FONT.deriveFont(Font.ITALIC)); // Use Font import
                setForeground(StyleConstants.SECONDARY_COLOR);
            }
            // Note: Verify that StyleConstants.PRIMARY_COLOR.darker() vs WHITE and WHITE vs TEXT_COLOR/SECONDARY_COLOR
            // provide sufficient contrast ratios (e.g., 4.5:1 for normal text, 3:1 for large text).
            return this;
        }
    }

    /**
     * Checks if the given date is occupied for the current user.
     * @param date The date to check.
     * @return true if the date is occupied, false otherwise.
     */
    private boolean isDateOccupied(LocalDate date) {
        if (date == null) {
            return false;
        }
        if (date.isBefore(LocalDate.now())) {
            return true; // Past dates are considered occupied
        }
        List<Appointment> userAppointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        for (Appointment appt : userAppointments) {
            if (appt.getAppointmentTime() != null) {
                LocalDate apptDate = LocalDate.of(
                    appt.getAppointmentTime().getYear(),
                    appt.getAppointmentTime().getMonthValue(),
                    appt.getAppointmentTime().getDayOfMonth()
                );
                if (apptDate.equals(date)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Constructs the PatientBookingPanel for the given user.
     * @param user The current user (patient).
     */
    public PatientBookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(0, 10));
        // Assuming StyleConstants.LIGHT_BG provides a light background
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Create a wrapper panel to hold all content and make it scrollable
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleConstants.LIGHT_BG); // Match main panel background

        // Title
        // Assuming StyleConstants.createLabel and StyleConstants.TITLE_FONT provide good contrast
        JLabel titleLabel = StyleConstants.createLabel("Book Your Appointment", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        // Assuming StyleConstants.createStyledPanel provides a styled panel
        JPanel mainContentPanel = StyleConstants.createStyledPanel(new BorderLayout(10, 10));

        // Add appointments list at the top
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        appointments.sort((a, b) -> a.getAppointmentTime().compareTo(b.getAppointmentTime()));
        System.out.println("~~~~~~~~~~##### List of Appointments for " + currentUser.getFName() + " " + currentUser.getLName());
        if (appointments.isEmpty()) {
        System.out.println("No appoint is found for user " + currentUser.getFName());}
        else{System.out.println("Appointments: " + appointments);}
        // Create a list model for appointments
        DefaultListModel<String> apptListModel = new DefaultListModel<>();
        if (appointments.isEmpty()) {
            apptListModel.addElement("No appointments found. Please book one below.");
        } else {
            apptListModel.addElement("Upcoming Appointments:");
        }
        for (Appointment appt : appointments) {
            String line = String.format("%s | Dr. %s | %s",
                appt.getAppointmentTime().toLocalDate() + " " + appt.getAppointmentTime().toLocalTime().withSecond(0).withNano(0),
                appt.getDoctorName(),
                appt.getStatus()
            );
            apptListModel.addElement(line);
        }
        JList<String> apptList = new JList<>(apptListModel);
        // Assuming StyleConstants.NORMAL_FONT provides a readable font
        apptList.setFont(StyleConstants.NORMAL_FONT);
        // Assuming StyleConstants.WHITE and StyleConstants.TEXT_COLOR provide good contrast
        apptList.setBackground(StyleConstants.WHITE);
        apptList.setForeground(StyleConstants.TEXT_COLOR);
        apptList.setBorder(BorderFactory.createTitledBorder("Your Appointments (Soonest First)")); // Border title color might need adjustment
        JScrollPane apptScrollPane = new JScrollPane(apptList);
        apptScrollPane.setPreferredSize(new Dimension(600, 90));
        apptScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(apptScrollPane, BorderLayout.NORTH);

        // Doctor search panel
        // Assuming StyleConstants.createStyledPanel and StyleConstants.createTitledBorder provide styling
        JPanel doctorSearchPanel = StyleConstants.createStyledPanel(new BorderLayout(10, 10));
        doctorSearchPanel.setBorder(StyleConstants.createTitledBorder("Find a Doctor"));

        // Search input panel
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel searchInputPanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        // Assuming StyleConstants.SECONDARY_COLOR provides a color for the border
        searchInputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, StyleConstants.SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Assuming StyleConstants.createLabel and StyleConstants.NORMAL_FONT provide good contrast
        JLabel searchLabel = StyleConstants.createLabel("Name or Specialty:", StyleConstants.NORMAL_FONT);
        JTextField searchField = new JTextField(25);
        // Assuming StyleConstants.styleTextField applies good contrast styling
        StyleConstants.styleTextField(searchField);

        // Assuming StyleConstants.createPrimaryButton and StyleConstants.BUTTON_FONT provide good contrast
        JButton searchButton = StyleConstants.createPrimaryButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 35));
        searchButton.setFont(StyleConstants.BUTTON_FONT);

        searchInputPanel.add(searchLabel);
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);

        // Doctor list panel
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel doctorListPanel = StyleConstants.createStyledPanel(new BorderLayout());
        doctorListPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        DefaultListModel<Object> doctorListModel = new DefaultListModel<>();
        JList<Object> doctorList = new JList<>(doctorListModel);
        doctorList.setCellRenderer(new DoctorListRenderer());
        // Assuming StyleConstants colors provide good contrast
        doctorList.setBackground(StyleConstants.WHITE);
        doctorList.setForeground(StyleConstants.TEXT_COLOR);
        doctorList.setSelectionBackground(StyleConstants.PRIMARY_COLOR.brighter()); // Verify contrast
        doctorList.setSelectionForeground(StyleConstants.WHITE); // Verify contrast
        // Assuming StyleConstants.NORMAL_FONT provides a readable font
        doctorList.setFont(StyleConstants.NORMAL_FONT);

        JScrollPane doctorListScrollPane = new JScrollPane(doctorList);
        doctorListScrollPane.setPreferredSize(new Dimension(400, 150));
        // Assuming StyleConstants.SECONDARY_COLOR provides a color for the border
        doctorListScrollPane.setBorder(BorderFactory.createLineBorder(StyleConstants.SECONDARY_COLOR, 1, true));

        // Add components to doctor search panel
        doctorSearchPanel.add(searchInputPanel, BorderLayout.NORTH);
        doctorListPanel.add(doctorListScrollPane, BorderLayout.CENTER);
        doctorSearchPanel.add(doctorListPanel, BorderLayout.CENTER);

        // Booking controls panel
        // Assuming StyleConstants.createSectionPanel provides styling
        JPanel bookingControlsPanel = StyleConstants.createSectionPanel("Appointment Details");
        bookingControlsPanel.setLayout(new BorderLayout(10, 15));

        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel dateTimePanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        // Date selection
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel datePanel = StyleConstants.createStyledPanel(new BorderLayout(5, 0));
        // Assuming StyleConstants.createLabel and StyleConstants.NORMAL_FONT provide good contrast
        JLabel dateLabel = StyleConstants.createLabel("Select Date:", StyleConstants.NORMAL_FONT);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setPreferredSize(new Dimension(120, 28)); // Reduce size
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "EEE, MMM d, yyyy"));
        dateEditor = ((JSpinner.DateEditor) dateSpinner.getEditor());
        dateEditor.getTextField().setEditable(false);
        dateEditor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        // Assuming StyleConstants.NORMAL_FONT provides a readable font
        dateEditor.getTextField().setFont(StyleConstants.NORMAL_FONT);
        // Assuming StyleConstants.WHITE and StyleConstants.INPUT_BORDER provide good contrast
        dateEditor.getTextField().setBackground(StyleConstants.WHITE);
        dateEditor.getTextField().setBorder(StyleConstants.INPUT_BORDER);

        dateSpinner.setValue(new Date());

        datePanel.add(dateLabel, BorderLayout.WEST);
        datePanel.add(dateSpinner, BorderLayout.CENTER);

        // Add change listener to update time slots when date changes
        dateSpinner.addChangeListener(e -> {
            if (selectedDoctor != null) {
                Date selectedDate = (Date) dateSpinner.getValue();
                LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (isDateOccupied(localDate)) {
                    JOptionPane.showMessageDialog(this, "This date is already booked. Please select another date.", "Date Unavailable", JOptionPane.WARNING_MESSAGE);
                    // Move to next day
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(selectedDate);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    dateSpinner.setValue(cal.getTime());
                    return;
                }
                populateTimePicker(timePicker, localDate, selectedDoctor);
            }
        });

        // Time selection
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel timePanel = StyleConstants.createStyledPanel(new BorderLayout(5, 0));
        // Assuming StyleConstants.createLabel and StyleConstants.NORMAL_FONT provide good contrast
        JLabel timeLabel = StyleConstants.createLabel("Select Time:", StyleConstants.NORMAL_FONT);

        timePicker = new JComboBox<>();
        // Assuming StyleConstants.NORMAL_FONT provides a readable font
        timePicker.setFont(StyleConstants.NORMAL_FONT);
        // Assuming StyleConstants.WHITE and StyleConstants.INPUT_BORDER provide good contrast
        timePicker.setBackground(StyleConstants.WHITE);
        timePicker.setBorder(StyleConstants.INPUT_BORDER);
        timePicker.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Assuming StyleConstants.NORMAL_FONT provides a readable font
                c.setFont(StyleConstants.NORMAL_FONT);
                // Assuming StyleConstants colors provide good contrast
                if (isSelected) {
                    c.setBackground(StyleConstants.PRIMARY_COLOR); // Verify contrast
                    c.setForeground(StyleConstants.WHITE); // Verify contrast
                } else {
                    c.setBackground(StyleConstants.WHITE); // Verify contrast
                    c.setForeground(StyleConstants.TEXT_COLOR); // Verify contrast
                }
                return c;
            }
        });
        timePicker.addItem("Select a doctor first");

        timePanel.add(timeLabel, BorderLayout.WEST);
        timePanel.add(timePicker, BorderLayout.CENTER);

        // Add date and time panels to dateTimePanel
        dateTimePanel.add(datePanel);
        dateTimePanel.add(timePanel);

        // Add dateTimePanel to booking controls
        bookingControlsPanel.add(dateTimePanel, BorderLayout.CENTER);

        // Center content panel
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel centerContentPanel = StyleConstants.createStyledPanel(new BorderLayout(0, 20));
        centerContentPanel.add(doctorSearchPanel, BorderLayout.CENTER);
        centerContentPanel.add(bookingControlsPanel, BorderLayout.SOUTH);

        // Add main content to the panel
        mainContentPanel.add(centerContentPanel, BorderLayout.CENTER);

        // Add padding around the main content
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel wrapperPanel = StyleConstants.createStyledPanel(new BorderLayout());
        wrapperPanel.add(mainContentPanel, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        contentPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Wrap content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // No border for scroll pane itself

        // Add scroll pane to the main panel
        add(scrollPane, BorderLayout.CENTER);

        // Doctor search button action
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            doctorListModel.clear();

            if (searchText.isEmpty()) {
                doctorListModel.addElement("Please enter a search term");
                return;
            }

            List<Doctor> foundDoctors = DatabaseAccessor.searchDoctors(searchText);

            if (foundDoctors.isEmpty()) {
                doctorListModel.addElement("No doctors found matching: " + searchText);
            } else {
                for (Doctor doc : foundDoctors) {
                    doctorListModel.addElement(doc);
                }
            }
        });

        // Doctor list selection listener
        doctorList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object selectedValue = doctorList.getSelectedValue();
                if (selectedValue instanceof Doctor doctor) {
                    this.selectedDoctor = doctor;
                    Date selectedDate = (Date) dateSpinner.getValue();
                    LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    if (!isDateOccupied(localDate)) {
                        populateTimePicker(timePicker, localDate, this.selectedDoctor);
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

        // Date spinner change listener is already set up in the initialization

        // Booking confirmation button
        // Assuming StyleConstants.createStyledPanel provides styling
        JPanel buttonPanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.CENTER));
        // Assuming StyleConstants.createPrimaryButton provides good contrast
        JButton bookingButton = StyleConstants.createPrimaryButton("Book Appointment");
        bookingButton.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(bookingButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to contentPanel
        bookingButton.addActionListener(e -> {
            String selectedTimeSlot = (String) timePicker.getSelectedItem();
            Date selectedDate = (Date) dateSpinner.getValue();
            LocalDate localDate = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            if (selectedTimeSlot == null || selectedTimeSlot.startsWith("No slots") || selectedTimeSlot.startsWith("Select")) {
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
                LocalDateTime appointmentDateTime = LocalDateTime.of(localDate, selectedTime);
                int doctorId = this.selectedDoctor.getUserID();
                Appointment newAppointment = new Appointment(currentUser.getUserID(), doctorId, appointmentDateTime, "Scheduled");
                // Add the appointment to the database
                DatabaseAccessor.addAppointment(newAppointment);
                JOptionPane.showMessageDialog(this, "Appointment booked for " + selectedDate + " at " + startTimeString);

                // --- Start: Code to re-populate appointments list ---
                // Fetch the updated list of appointments
                List<Appointment> updatedAppointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
                updatedAppointments.sort((a, b) -> a.getAppointmentTime().compareTo(b.getAppointmentTime()));

                // Clear the existing list model
                apptListModel.clear();

                // Add the header back
                if (updatedAppointments.isEmpty()) {
                    apptListModel.addElement("No appointments found. Please book one below.");
                } else {
                    apptListModel.addElement("Upcoming Appointments:");
                }

                // Add the updated appointments to the list model
                for (Appointment appt : updatedAppointments) {
                    String line = String.format("%s | Dr. %s | %s",
                        appt.getAppointmentTime().toLocalDate() + " " + appt.getAppointmentTime().toLocalTime().withSecond(0).withNano(0),
                        appt.getDoctorName(),
                        appt.getStatus()
                    );
                    apptListModel.addElement(line);
                }
                // --- End: Code to re-populate appointments list ---

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Returns all available time slots for a day.
     * @return List of time slot strings.
     */
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

    /**
     * Populates the time picker with available slots for the selected doctor and date.
     * @param timePickerComboBox The combo box to populate.
     * @param selectedDate The selected date.
     * @param doctor The selected doctor.
     */
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
            boolean isOccupied = false;
            for (LocalDateTime occupiedTime : occupiedAppointments) {
                if (occupiedTime.toLocalDate().equals(selectedDate) && occupiedTime.toLocalTime().equals(slotStartTime)) {
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