package com.example.OnlineMedicalAppointment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    @Test
    void testAdminFields() {
        Admin a = new Admin(1, "Alice", "Admin", "alice", "555-0000");
        assertEquals(1, a.getUserID());
        assertEquals("Alice", a.getFName());
        assertEquals("Admin", a.getLName());
        assertEquals("alice", a.getUsername());
        assertEquals("Admin", a.getUserType());
        assertEquals("555-0000", a.getPhoneNumber());
    }
}
