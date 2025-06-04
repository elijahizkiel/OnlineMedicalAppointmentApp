package com.example.OnlineMedicalAppointment.ui;

import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ProfilePanelTest {
    @Test
    void testProfilePanelCreation() {
        User user = new Patient("Test", "User", "testuser", "pass", "1234567890");
        ProfilePanel panel = new ProfilePanel(user);
        assertNotNull(panel);
        assertTrue(panel instanceof JPanel);
    }
}
