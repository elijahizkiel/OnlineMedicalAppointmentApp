package com.example.OnlineMedicalAppointment.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Appointment class.
 */
class AppointmentTest {
    @Test
    void testAppointmentFields() {
        // Adjust constructor if Appointment class signature has changed
        LocalDateTime now = LocalDateTime.now();
        Appointment appt = new Appointment(1, 2, now, "Pending");
        assertEquals(1, appt.getPatientID());
        assertEquals(2, appt.getDoctorID());
        assertEquals(now, appt.getAppointmentTime());
        assertEquals("Pending", appt.getStatus());
    }
}
