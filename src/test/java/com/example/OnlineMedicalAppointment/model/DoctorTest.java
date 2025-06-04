package com.example.OnlineMedicalAppointment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {
    @Test
    void testDoctorFields() {
        Doctor d = new Doctor(1, "Jane", "Smith", "jsmith", "Cardiology", "555-1234");
        assertEquals(1, d.getUserID());
        assertEquals("Jane", d.getFName());
        assertEquals("Smith", d.getLName());
        assertEquals("jsmith", d.getUsername());
        assertEquals("Doctor", d.getUserType());
        assertEquals("Cardiology", d.getSpecialty());
        assertEquals("555-1234", d.getPhoneNumber());
    }
}
