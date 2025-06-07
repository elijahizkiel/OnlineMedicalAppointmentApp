package com.example.OnlineMedicalAppointment.ui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Unit tests for the PatientBookingPanel class.
 */
class PatientBookingPanelTest {
    private PatientBookingPanel panel;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        dummyUser = new Patient("Test", "User", "testuser","pass", "1234567890");
        panel = new PatientBookingPanel(dummyUser);
    }

    @Test
    void testAllTimeSlotsReturned() {
        List<String> slots = panel.getAllTimeSlots();
        assertNotNull(slots);
        assertFalse(slots.isEmpty());
        // Should contain at least 9 slots for a typical 8-17 schedule
        assertTrue(slots.size() >= 9);
    }

    @Test
    void testTimePickerComboBoxUpdates() throws Exception {
        // Access private timePicker field
        JComboBox<String> timePicker = getTimePicker();
        // Simulate selecting a doctor and a date
        Doctor doctor = new Doctor("Doc", "Tor", "doctor1", "pass", "Doctor", "Cardiology", "1234567890");
        LocalDate date = LocalDate.now().plusDays(1);
        // Use reflection to call the private populateTimePicker method
        var method = PatientBookingPanel.class.getDeclaredMethod(
                "populateTimePicker", JComboBox.class, LocalDate.class, Doctor.class);
        method.setAccessible(true);
        method.invoke(panel, timePicker, date, doctor);
        assertTrue(timePicker.getItemCount() > 0);
    }

    // Helper to access private timePicker field via reflection
    @SuppressWarnings("unchecked")
    private JComboBox<String> getTimePicker() {
        try {
            var field = PatientBookingPanel.class.getDeclaredField("timePicker");
            field.setAccessible(true);
            return (JComboBox<String>) field.get(panel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}