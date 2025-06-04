package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Panel for booking appointments as a patient.
 */
public class PatientBookingPanel extends JPanel {
    private final User currentUser;
    private final JComboBox<String> doctorPicker;
    private final JXDatePicker datePicker;
    private final JComboBox<String> timePicker;
    private List<Appointment> currentDoctorAppointments = new ArrayList<>();
    private List<Date> occupiedDates = new ArrayList<>();
    private List<Date> fullyOccupiedDates = new ArrayList<>();
    private final JTextField specialtyField;

    public PatientBookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // Doctor selection/filter panel
        JPanel doctorPanel = new JPanel();
        doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.X_AXIS));
        doctorPanel.add(new JLabel("Select Doctor:"));
        this.doctorPicker = new JComboBox<>();
        doctorPanel.add(this.doctorPicker);
        doctorPanel.add(new JLabel("Filter by Specialty:"));
        specialtyField = new JTextField(15);
        doctorPanel.add(specialtyField);
        JButton searchButton = new JButton("Search");
        doctorPanel.add(searchButton);

        // North panel (title + doctor selection)
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(new JLabel("This is the Patient Booking Panel."));
        northPanel.add(doctorPanel);
        add(northPanel, BorderLayout.NORTH);

        // Booking controls (date + time)
        JPanel bookingControlsPanel = new JPanel();
        bookingControlsPanel.setLayout(new BoxLayout(bookingControlsPanel, BoxLayout.X_AXIS));
        datePicker = new JXDatePicker();
        datePicker.setDate(new Date());
        timePicker = new JComboBox<>();
        bookingControlsPanel.add(new JLabel("Select Date:"));
        bookingControlsPanel.add(datePicker);
        bookingControlsPanel.add(new JLabel("Select Time:"));
        bookingControlsPanel.add(timePicker);
        add(bookingControlsPanel, BorderLayout.CENTER);

        // Populate initial doctor list
        populateDoctorPicker(null);

        // Listeners
        searchButton.addActionListener(e -> {
            String specialty = specialtyField.getText().trim();
            populateDoctorPicker(specialty.isEmpty() ? null : specialty);
        });

        doctorPicker.addActionListener(e -> {
            updateAppointmentsAndCalendar();
        });

        datePicker.addActionListener(e -> {
            Date selectedDate = datePicker.getDate();
            if (selectedDate != null) {
                populateTimePicker(timePicker, selectedDate);
            } else {
                timePicker.removeAllItems();
            }
        });

        // Initial load for first doctor
        updateAppointmentsAndCalendar();
    }

    /**
     * Populates the doctor picker JComboBox, optionally filtering by specialty.
     */
    private void populateDoctorPicker(String specialty) {
        doctorPicker.removeAllItems();
        // Placeholder data
        List<String> dummyDoctors = new ArrayList<>();
        dummyDoctors.add("Dr. Smith (Cardiology)");
        dummyDoctors.add("Dr. Jones (Pediatrics)");
        dummyDoctors.add("Dr. Williams (General Practice)");
        dummyDoctors.add("Dr. Brown (Cardiology)");

        for (String doctor : dummyDoctors) {
            if (specialty == null || specialty.isEmpty() ||
                doctor.toLowerCase().contains(specialty.toLowerCase())) {
                doctorPicker.addItem(doctor);
            }
        }
        if (doctorPicker.getItemCount() == 0) {
            doctorPicker.addItem("No doctors found");
        } else {
            doctorPicker.setSelectedIndex(0);
        }
    }

    /**
     * Updates appointments, occupied/fully occupied dates, and refreshes calendar and time picker.
     */
    private void updateAppointmentsAndCalendar() {
        String selectedDoctor = (String) doctorPicker.getSelectedItem();
        if (selectedDoctor == null || selectedDoctor.equals("No doctors found")) {
            currentDoctorAppointments = new ArrayList<>();
            occupiedDates = new ArrayList<>();
            fullyOccupiedDates = new ArrayList<>();
            datePicker.setMonthView(new MonthView());
            timePicker.removeAllItems();
            return;
        }
        // Simulate fetching appointments for the selected doctor
        try {
            List<Appointment> allAppointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
            currentDoctorAppointments = allAppointments.stream()
                .filter(appt -> appt.getDoctorName() != null && selectedDoctor.startsWith(appt.getDoctorName()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            currentDoctorAppointments = new ArrayList<>();
        }

        // Occupied dates
        Set<Date> datesWithAppointments = new HashSet<>();
        for (Appointment appt : currentDoctorAppointments) {
            if (appt.getAppointmentTime() != null) {
                Calendar cal = Calendar.getInstance();
                Date dateFromLocalDateTime = java.util.Date.from(appt.getAppointmentTime().atZone(java.time.ZoneId.systemDefault()).toInstant());
                cal.setTime(dateFromLocalDateTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                datesWithAppointments.add(cal.getTime());
            }
        }
        occupiedDates = new ArrayList<>(datesWithAppointments);

        // Simulate fully occupied days (if all slots are booked)
        fullyOccupiedDates = new ArrayList<>();
        for (Date date : occupiedDates) {
            if (isDayFullyOccupied(date)) {
                fullyOccupiedDates.add(date);
            }
        }

        // Update calendar highlighters and disabling
        updateDatePickerHighlighters();

        // Update time picker for the selected date
        Date selectedDate = datePicker.getDate();
        if (selectedDate != null) {
            populateTimePicker(timePicker, selectedDate);
        } else {
            timePicker.removeAllItems();
        }
    }

    /**
     * Updates the date picker's highlighters and disables fully occupied days.
     */
    private void updateDatePickerHighlighters() {
        // Use only the default highlighter (remove custom highlighter logic)
        // Custom MonthView to disable fully occupied days
        MonthView monthView = new MonthView() {
            @Override
            public boolean isDateEnabled(Date date) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                cal1.set(Calendar.HOUR_OF_DAY, 0);
                cal1.set(Calendar.MINUTE, 0);
                cal1.set(Calendar.SECOND, 0);
                cal1.set(Calendar.MILLISECOND, 0);
                Date normalizedDate = cal1.getTime();
                for (Date fullyOccupiedDate : fullyOccupiedDates) {
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(fullyOccupiedDate);
                    cal2.set(Calendar.HOUR_OF_DAY, 0);
                    cal2.set(Calendar.MINUTE, 0);
                    cal2.set(Calendar.SECOND, 0);
                    cal2.set(Calendar.MILLISECOND, 0);
                    Date normalizedFullyOccupiedDate = cal2.getTime();
                    if (normalizedDate.equals(normalizedFullyOccupiedDate)) {
                        return false;
                    }
                }
                return super.isDateEnabled(date);
            }
        };
        // Copy settings from the original MonthView
        MonthView orig = datePicker.getMonthView();
        monthView.setFirstDisplayedMonth(orig.getFirstDisplayedMonth());
        monthView.setLastDisplayedMonth(orig.getLastDisplayedMonth());
        monthView.setSelection(orig.getSelection());
        monthView.setPropertiesFrom(orig);
        datePicker.setMonthView(monthView);
        datePicker.removeHighlighters(); // Remove any custom highlighters
        // Do not add any custom highlighter, rely on default
    }

    /**
     * Returns all possible time slots for a day.
     */
    public List<String> getAllTimeSlots() {
        List<String> allSlots = new ArrayList<>();
        for (int hour = 8; hour < 17; hour++) {
            allSlots.add(String.format("%02d:00 - %02d:30", hour, hour));
            if (hour < 16) {
                allSlots.add(String.format("%02d:30 - %02d:00", hour, hour + 1));
            }
        }
        return allSlots;
    }

    /**
     * Checks if a day is fully occupied (all slots booked).
     */
    public boolean isDayFullyOccupied(Date date) {
        List<String> allSlots = getAllTimeSlots();
        Set<String> bookedSlots = getBookedSlotsForDate(date);
        return bookedSlots.size() >= allSlots.size();
    }

    /**
     * Gets booked slots for a given date for the current doctor.
     */
    private Set<String> getBookedSlotsForDate(Date date) {
        Set<String> booked = new HashSet<>();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        for (Appointment appt : currentDoctorAppointments) {
            if (appt.getAppointmentTime() != null) {
                Date apptDate = java.util.Date.from(appt.getAppointmentTime().atZone(java.time.ZoneId.systemDefault()).toInstant());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(apptDate);
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);
                if (cal1.getTime().equals(cal2.getTime())) {
                    // Simulate mapping appointment time to slot string
                    int hour = apptDate.getHours();
                    int minute = apptDate.getMinutes();
                    String slot;
                    if (minute < 30) {
                        slot = String.format("%02d:00 - %02d:30", hour, hour);
                    } else {
                        slot = String.format("%02d:30 - %02d:00", hour, hour + 1);
                    }
                    booked.add(slot);
                }
            }
        }
        return booked;
    }

    /**
     * Populates the time picker, marking occupied slots orange and disabling selection.
     */
    private void populateTimePicker(JComboBox<String> timePicker, Date date) {
        timePicker.removeAllItems();
        List<String> allSlots = getAllTimeSlots();
        Set<String> bookedSlots = getBookedSlotsForDate(date);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String slot : allSlots) {
            model.addElement(slot);
        }
        timePicker.setModel(model);
        timePicker.setRenderer(new ListCellRenderer<String>() {
            private final JLabel label = new JLabel();
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                label.setText(value);
                if (bookedSlots.contains(value)) {
                    label.setForeground(Color.GRAY);
                    label.setBackground(Color.ORANGE);
                    label.setEnabled(false);
                } else {
                    label.setForeground(isSelected ? Color.WHITE : Color.BLACK);
                    label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                    label.setEnabled(true);
                }
                label.setOpaque(true);
                return label;
            }
        });
        // Disable selection of occupied slots
        timePicker.addActionListener(e -> {
            String selected = (String) timePicker.getSelectedItem();
            if (selected != null && bookedSlots.contains(selected)) {
                timePicker.setSelectedIndex(-1);
            }
        });
    }
}