package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor; // Assuming doctors chat with patients
import com.example.OnlineMedicalAppointment.model.User; // Assuming you need to select a patient to chat with

/**
 * Main panel for the doctor's view.
 * Contains tabs for schedule, patient management, and messaging.
 */
public class DoctorMainPanel extends JPanel {
    private User doctorUser;
    private JTabbedPane tabbedPane;

    /**
     * Constructs the DoctorMainPanel for the given doctor user.
     * @param doctorUser The current user (doctor).
     */
    public DoctorMainPanel(User doctorUser) {
        this.doctorUser = doctorUser;
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Add existing tabs
        tabbedPane.addTab("Schedule", new DoctorSchedulePanel(doctorUser));
        // ...add other existing tabs...

        // Add Chat tab
        // This is a simplified example. A real implementation would likely
        // have a list of patients the doctor can chat with, and selecting a patient
        // would open a ChatPanel for that specific patient.
        // For demonstration, let's assume the doctor can chat with the first patient found.
        List<User> patients = DatabaseAccessor.getUsersByType("Patient");
        if (!patients.isEmpty()) {
            User firstPatient = patients.get(0); // Example: chat with the first patient
            tabbedPane.addTab("Chat with " + firstPatient.getFName(), new ChatPanel(doctorUser, firstPatient));
        } else {
            tabbedPane.addTab("Chat", new JLabel("") );
        }


        add(tabbedPane, BorderLayout.CENTER);
    }
    // ...existing methods...
}