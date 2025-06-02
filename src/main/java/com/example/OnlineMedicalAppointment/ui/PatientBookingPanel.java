package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.decorator.ColorHighlighter; // Needed for TODO
import org.jdesktop.swingx.decorator.HighlightPredicate; // Needed for TODO
import java.awt.Color; // Needed for TODO

public class PatientBookingPanel extends JPanel {

    private User currentUser;

    public PatientBookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("This is the Patient Booking Panel."), BorderLayout.NORTH);
        // TODO: Add components for searching doctors and calendar booking

        JPanel bookingControlsPanel = new JPanel();
        // Use a layout manager suitable for arranging date and time side-by-side
        bookingControlsPanel.setLayout(new BoxLayout(bookingControlsPanel, BoxLayout.X_AXIS)); // Or FlowLayout

        // JXDatePicker for date selection
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setDate(new Date()); // Set default to today

        // TODO: Implement custom logic for JXDatePicker to mark occupied days orange
        // and disable picking fully occupied days based on dbAccessor data.
        // This typically involves creating a custom org.jdesktop.swingx.decorator.Highlighter
        // and potentially overriding isDateEnabled in the MonthView or using a custom DateSelectionModel.
        // This is a complex task requiring iteration through dates and checking database for each.
        // Example of adding a basic highlighter (does not check DB):
        // datePicker.getMonthView().addHighlighter(new ColorHighlighter(HighlightPredicate.IS_TODAY, Color.ORANGE));


        // JComboBox for time selection
        JComboBox<String> timePicker = new JComboBox<>();
        // Initial population (e.g., all possible slots)
        populateTimePicker(timePicker, new Date()); // Populate for today initially

        // Add listener to datePicker to update timePicker when the selected date changes
        datePicker.addActionListener(e -> {
            Date selectedDate = datePicker.getDate();
            if (selectedDate != null) {
            populateTimePicker(timePicker, selectedDate);
            } else {
            timePicker.removeAllItems(); // Clear if no date is selected
            }
        });

        // Add components to the booking controls panel
        bookingControlsPanel.add(new JLabel("Select Date:"));
        bookingControlsPanel.add(datePicker);
        bookingControlsPanel.add(new JLabel("Select Time:"));
        bookingControlsPanel.add(timePicker);

        // Add the controls panel to the main panel
        add(bookingControlsPanel, BorderLayout.CENTER);

        // --- Helper methods and Placeholder DatabaseAccessor ---
        // These should ideally be in separate classes or methods within this class

        } // End of constructor

        

        // Method to get all available time slots for a day ( work hours excluding booked hours)
        // This should ideally be configurable or fetched from DB based on doctor/clinic hours
        public List<String> getAllTimeSlots() {
             List<String> allSlots = new java.util.ArrayList<>();
             // Simulate standard work hours slots (e.g., 9 AM to 5 PM, 30 min intervals)
             for (int hour = 8; hour < 17; hour++) {
                allSlots.add(String.format("%02d:00 - %02d:30", hour, hour));
             if (hour < 16) { // Don't add 17:00 - 17:30 if work ends at 17:00
                allSlots.add(String.format("%02d:30 - %02d:00", hour, hour + 1));
             }
             }
             return allSlots;
        }

        // TODO: Add a method to check if a whole day is fully occupied
        // public boolean isDayFullyOccupied(Date date) { ... }
        

        // Helper method to populate the time picker
        // This method needs to fetch occupied slots and mark/disable them in the JComboBox
        private void populateTimePicker(JComboBox<String> timePicker, Date date) {
        timePicker.removeAllItems();
        // List<String> allSlots = DatabaseAccessor.getAllTimeSlots();
        // List<String> occupiedSlots = DatabaseAccessor.getOccupiedSlots(date);

        // TODO: Implement a custom ListCellRenderer for JComboBox to mark occupied slots orange.
        // TODO: Implement a custom ComboBoxModel or use a custom renderer/listener
        // to disable selection of occupied slots.

        // For now, just add all slots. Marking/disabling requires more code.
        // for (String slot : allSlots) {
            // timePicker.addItem(slot);
        // }

        // Example of how you might use the occupied slots list:
        // System.out.println("Occupied slots for " + date + ": " + occupiedSlots);
        // You would use this list within your custom renderer/model to apply colors/disabling.
        }
    }