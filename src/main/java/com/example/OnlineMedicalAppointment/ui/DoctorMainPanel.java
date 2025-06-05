package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Main panel for the doctor's view.
 * Contains tabs for schedule, patient management, and messaging.
 */
public class DoctorMainPanel extends JPanel {
    /**
     * Constructs the DoctorMainPanel for the given doctor user.
     * @param doctorUser The current user (doctor).
     */
    public DoctorMainPanel(User doctorUser) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Schedule", new DoctorSchedulePanel(doctorUser));
        // Add other tabs as needed
        tabbedPane.addTab("Messages", new DoctorChatPanel(doctorUser)); // Add chat tab

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }
}