package com.example.OnlineMedicalAppointment.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Patient class.
 */
class PatientTest {
    @Test
    void testPatientFields() {
        Patient p = new Patient("John", "Doe", "johndoe","pass", "1234567890");
        assertEquals("John", p.getFName());
        assertEquals("Doe", p.getLName());
        assertEquals("johndoe", p.getUsername());
        assertEquals("1234567890", p.getPhoneNumber());
        assertEquals("Patient", p.getUserType());
    }
}
