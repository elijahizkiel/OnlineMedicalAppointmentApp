package com.example.OnlineMedicalAppointment.ui;

import javax.swing.JFrame;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the LoginFrame class.
 */
class LoginFrameTest {
    @Test
    void testLoginFrameComponents() {
        LoginFrame frame = new LoginFrame();
        assertNotNull(frame);
        assertTrue(frame instanceof JFrame);
    }
}
