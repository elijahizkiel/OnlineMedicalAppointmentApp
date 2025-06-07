package com.example.OnlineMedicalAppointment.ui;

import javax.swing.JFrame;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SignupFrame class.
 */
class SignupFrameTest {
    @Test
    void testSignupFrameComponents() {
        SignupFrame frame = new SignupFrame();
        assertNotNull(frame);
        assertTrue(frame instanceof JFrame);
    }
}
