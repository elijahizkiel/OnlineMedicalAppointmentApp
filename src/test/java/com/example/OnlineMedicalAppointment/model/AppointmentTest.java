package com.example.OnlineMedicalAppointment.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {
    @Test
    void testAppointmentFields() {
        LocalDateTime now = LocalDateTime.now();
        Appointment appt = new Appointment(1, 2, now, "Pending");
        assertEquals(1, appt.getMadeBy());
        assertEquals(2, appt.getDoctorID());
        assertEquals(now, appt.getAppointmentTime());
        assertEquals("Pending", appt.getStatus());
    }
}
