package com.example.OnlineMedicalAppointment.ui;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class SignupFrameTest {
    @Test
    void testSignupFrameComponents() {
        SignupFrame frame = new SignupFrame();
        assertNotNull(frame);
        assertTrue(frame instanceof JFrame);
    }
}
