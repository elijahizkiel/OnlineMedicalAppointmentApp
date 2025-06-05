package com.example.OnlineMedicalAppointment.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DoctorTest {
    @Test
    void testDoctorFields() {
        Doctor d = new Doctor(1, "Jane", "Smith", "jsmith", "pass","Doctor", "Cardiology", "555-1234");
        assertEquals(1, d.getUserID());
        assertEquals("Jane", d.getFName());
        assertEquals("Smith", d.getLName());
        assertEquals("jsmith", d.getUsername());
        assertEquals("Doctor", d.getUserType());
        assertEquals("Cardiology", d.getSpecialty());
        assertEquals("555-1234", d.getPhoneNumber());
    }
}
