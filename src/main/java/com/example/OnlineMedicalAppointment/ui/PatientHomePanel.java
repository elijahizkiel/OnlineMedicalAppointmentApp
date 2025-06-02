package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

import javax.swing.BoxLayout;
import javax.swing.Box;
import java.util.List;

public class PatientHomePanel extends JPanel {

    private final User currentUser;

    public PatientHomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("Welcome, " + currentUser.getFName() + "! This is the Patient Home Panel."), BorderLayout.NORTH);
        // Assuming Doctor class is imported or in the same package

        // Panel for main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Arrange vertically

        // 1. Show appointment status
        JLabel appointmentStatusLabel;
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        if ( appointments != null && !appointments.isEmpty()) {
            appointmentStatusLabel = new JLabel("You have upcoming appointments.");
        } else {
            appointmentStatusLabel = new JLabel("You have no upcoming appointments.");
        }
        contentPanel.add(appointmentStatusLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(new JLabel("Available Doctors:"));

        List<Doctor> sampleDoctors = DatabaseAccessor.getDoctors();

        if (sampleDoctors != null && !sampleDoctors.isEmpty()) {
            for (Doctor doctor : sampleDoctors) {
            JLabel doctorLabel = new JLabel(
                "Dr. " + doctor.getFName() + " - " + doctor.getSpecialty() + " - Hours: " + "08:00AM - 05:30PM" );//doctor.getWorkHours()
            // Assuming getWorkHours() returns a string like "08:00AM - 05:30PM"
            doctorLabel.setAlignmentX(LEFT_ALIGNMENT); // Align left
            contentPanel.add(doctorLabel);
            }
        } else {
            contentPanel.add(new JLabel("No sample doctors available at the moment."));
        }
        // Add the content panel to the center
        add(contentPanel, BorderLayout.CENTER);
    }
}
