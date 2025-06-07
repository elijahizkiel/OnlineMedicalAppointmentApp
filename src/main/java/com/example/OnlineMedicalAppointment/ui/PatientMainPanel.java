package com.example.OnlineMedicalAppointment.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor; // To get list of doctors
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Doctor; // Assuming patients chat with doctors


public class PatientMainPanel extends JPanel {
    private final User patientUser;
    private final JTabbedPane tabbedPane;

    public PatientMainPanel(User patientUser) {
        this.patientUser = patientUser;
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Add existing tabs
        tabbedPane.addTab("Book Appointment", new PatientBookingPanel(patientUser));
        // ...add other existing tabs...

        // Add Chat tab
        // This is a simplified example. A real implementation would likely
        // have a list of doctors the patient can chat with, and selecting a doctor
        // would open a ChatPanel for that specific doctor.
        // For demonstration, let's assume the patient can chat with the first doctor found.
        List<User> doctors = new ArrayList<>(); // Assuming you have a method to get doctors 
        DatabaseAccessor.getDoctors().forEach(e -> {
            if (e instanceof Doctor) {
                doctors.add(e);
            }
        });
        if (!doctors.isEmpty()) {
            User firstDoctor = doctors.get(0); // Example: chat with the first doctor
            tabbedPane.addTab("Chat with Dr. " + firstDoctor.getLName(), new ChatPanel(patientUser, firstDoctor));
        } else {
             tabbedPane.addTab("Chat", new JLabel("No doctors available to chat with.", SwingConstants.CENTER));
        }


        add(tabbedPane, BorderLayout.CENTER);
    }
    // ...existing methods...
}
