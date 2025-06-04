package com.example.OnlineMedicalAppointment.ui;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientBookingPanelTest {
    private PatientBookingPanel panel;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        dummyUser = new Patient("Test", "User", "testuser","pass", "1234567890");
        panel = new PatientBookingPanel(dummyUser);
    }

    @Test
    void testDoctorPickerPopulated() {
        JComboBox<String> doctorPicker = getDoctorPicker();
        assertTrue(doctorPicker.getItemCount() > 0);
    }

    @Test
    void testTimeSlotsReturned() {
        List<String> slots = panel.getAllTimeSlots();
        assertNotNull(slots);
        assertFalse(slots.isEmpty());
    }

    @Test
    void testIsDayFullyOccupiedReturnsBoolean() {
        Date today = new Date();
        boolean result = panel.isDayFullyOccupied(today);
        assertNotNull(result);
    }

    // Helper to access private field via reflection (for test only)
    private JComboBox<String> getDoctorPicker() {
        try {
            java.lang.reflect.Field field = PatientBookingPanel.class.getDeclaredField("doctorPicker");
            field.setAccessible(true);
            return (JComboBox<String>) field.get(panel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
