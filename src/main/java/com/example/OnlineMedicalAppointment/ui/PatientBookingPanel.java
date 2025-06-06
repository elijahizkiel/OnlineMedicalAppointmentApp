package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private User currentUser;
    private JSpinner dateSpinner;
    private JComboBox<String> timePicker;
    private Doctor selectedDoctor;
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
            setFont(StyleConstants.NORMAL_FONT);
            
            if (value instanceof Doctor doctor) {
                setText(doctor.getFName() + " " + doctor.getLName() + " (" + doctor.getSpecialty() + ")");
                if (isSelected) {
                    setBackground(StyleConstants.PRIMARY_COLOR.darker());
                    setForeground(StyleConstants.WHITE);
                } else {
                    setBackground(StyleConstants.WHITE);
                    setForeground(StyleConstants.TEXT_COLOR);
                }
            } else if (value instanceof String text) {
                setText(text);
                setFont(StyleConstants.NORMAL_FONT.deriveFont(java.awt.Font.ITALIC));
                setForeground(StyleConstants.SECONDARY_COLOR);
            }
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
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Create a wrapper panel to hold all content and make it scrollable
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleConstants.LIGHT_BG);
        
        // Title
        JLabel titleLabel = StyleConstants.createLabel("Book Your Appointment", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainContentPanel = StyleConstants.createStyledPanel(new BorderLayout(10, 10));
        
        // Add appointments list at the top
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        appointments.sort((a, b) -> a.getAppointmentTime().compareTo(b.getAppointmentTime()));
        DefaultListModel<String> apptListModel = new DefaultListModel<>();
        for (Appointment appt : appointments) {
            String line = String.format("%s | Dr. %s | %s",
                appt.getAppointmentTime().toLocalDate() + " " + appt.getAppointmentTime().toLocalTime().withSecond(0).withNano(0),
                appt.getDoctorName(),
                appt.getStatus()
            );
            apptListModel.addElement(line);
        }
        JList<String> apptList = new JList<>(apptListModel);
        apptList.setFont(StyleConstants.NORMAL_FONT);
        apptList.setBackground(StyleConstants.WHITE);
        apptList.setBorder(BorderFactory.createTitledBorder("Your Appointments (Soonest First)"));
        JScrollPane apptScrollPane = new JScrollPane(apptList);
        apptScrollPane.setPreferredSize(new Dimension(600, 90));
        apptScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(apptScrollPane, BorderLayout.NORTH);

        // Doctor search panel
        JPanel doctorSearchPanel = StyleConstants.createStyledPanel(new BorderLayout(10, 10));
        doctorSearchPanel.setBorder(StyleConstants.createTitledBorder("Find a Doctor"));

        // Search input panel
        JPanel searchInputPanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchInputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, StyleConstants.SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel searchLabel = StyleConstants.createLabel("Name or Specialty:", StyleConstants.NORMAL_FONT);
        JTextField searchField = new JTextField(25);
        StyleConstants.styleTextField(searchField);
        
        JButton searchButton = StyleConstants.createPrimaryButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 35));
        searchButton.setFont(StyleConstants.BUTTON_FONT);
        
        searchInputPanel.add(searchLabel);
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);

        // Doctor list panel
        JPanel doctorListPanel = StyleConstants.createStyledPanel(new BorderLayout());
        doctorListPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        
        DefaultListModel<Object> doctorListModel = new DefaultListModel<>();
        JList<Object> doctorList = new JList<>(doctorListModel);
        doctorList.setCellRenderer(new DoctorListRenderer());
        doctorList.setBackground(StyleConstants.WHITE);
        doctorList.setSelectionBackground(StyleConstants.PRIMARY_COLOR.brighter());
        doctorList.setSelectionForeground(StyleConstants.WHITE);
        doctorList.setFont(StyleConstants.NORMAL_FONT);
        
        JScrollPane doctorListScrollPane = new JScrollPane(doctorList);
        doctorListScrollPane.setPreferredSize(new Dimension(400, 150));
        doctorListScrollPane.setBorder(BorderFactory.createLineBorder(StyleConstants.SECONDARY_COLOR, 1, true));
        
        // Add components to doctor search panel
        doctorSearchPanel.add(searchInputPanel, BorderLayout.NORTH);
        doctorListPanel.add(doctorListScrollPane, BorderLayout.CENTER);
        doctorSearchPanel.add(doctorListPanel, BorderLayout.CENTER);

        // Booking controls panel
        JPanel bookingControlsPanel = StyleConstants.createSectionPanel("Appointment Details");
        bookingControlsPanel.setLayout(new BorderLayout(10, 15));
        
        JPanel dateTimePanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        // Date selection
        JPanel datePanel = StyleConstants.createStyledPanel(new BorderLayout(5, 0));
        JLabel dateLabel = StyleConstants.createLabel("Select Date:", StyleConstants.NORMAL_FONT);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setPreferredSize(new Dimension(120, 28)); // Reduce size
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "EEE, MMM d, yyyy"));
        dateEditor = ((JSpinner.DateEditor) dateSpinner.getEditor());
        dateEditor.getTextField().setEditable(false);
        dateEditor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        dateEditor.getTextField().setFont(StyleConstants.NORMAL_FONT);
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
        JPanel timePanel = StyleConstants.createStyledPanel(new BorderLayout(5, 0));
        JLabel timeLabel = StyleConstants.createLabel("Select Time:", StyleConstants.NORMAL_FONT);
        
        timePicker = new JComboBox<>();
        timePicker.setFont(StyleConstants.NORMAL_FONT);
        timePicker.setBackground(StyleConstants.WHITE);
        timePicker.setBorder(StyleConstants.INPUT_BORDER);
        timePicker.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(StyleConstants.NORMAL_FONT);
                if (isSelected) {
                    c.setBackground(StyleConstants.PRIMARY_COLOR);
                    c.setForeground(StyleConstants.WHITE);
                } else {
                    c.setBackground(StyleConstants.WHITE);
                    c.setForeground(StyleConstants.TEXT_COLOR);
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
        JPanel centerContentPanel = StyleConstants.createStyledPanel(new BorderLayout(0, 20));
        centerContentPanel.add(doctorSearchPanel, BorderLayout.CENTER);
        centerContentPanel.add(bookingControlsPanel, BorderLayout.SOUTH);
        
        // Add main content to the panel
        mainContentPanel.add(centerContentPanel, BorderLayout.CENTER);
        
        // Add padding around the main content
        JPanel wrapperPanel = StyleConstants.createStyledPanel(new BorderLayout());
        wrapperPanel.add(mainContentPanel, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        contentPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Wrap content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

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
        JPanel buttonPanel = StyleConstants.createStyledPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bookingButton = StyleConstants.createPrimaryButton("Book Appointment");
        bookingButton.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(bookingButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
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