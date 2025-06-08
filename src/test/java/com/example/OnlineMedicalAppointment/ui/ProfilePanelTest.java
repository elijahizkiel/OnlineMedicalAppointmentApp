package com.example.OnlineMedicalAppointment.ui;

import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.OnlineMedicalAppointment.model.Patient;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Unit tests for the ProfilePanel class.
 */
class ProfilePanelTest {
    @Test
    void testProfilePanelCreation() {
        User user = new Patient("Test", "User", "testuser", "pass", "1234567890");
        ProfilePanel panel = new ProfilePanel(user);
        assertNotNull(panel);
        assertTrue(panel instanceof JPanel);
    }
}
