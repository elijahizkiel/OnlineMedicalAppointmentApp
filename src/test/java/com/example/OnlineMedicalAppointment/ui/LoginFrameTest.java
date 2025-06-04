package com.example.OnlineMedicalAppointment.ui;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class LoginFrameTest {
    @Test
    void testLoginFrameComponents() {
        LoginFrame frame = new LoginFrame();
        assertNotNull(frame);
        assertTrue(frame instanceof JFrame);
    }
}
