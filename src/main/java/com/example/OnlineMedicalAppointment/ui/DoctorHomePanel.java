package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;
import java.util.List;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.Appointment;

/**
 * Panel for displaying the doctor's home view and appointments.
 */
public class DoctorHomePanel extends JPanel {

    private User currentUser;

    /**
     * Constructs the DoctorHomePanel for the given user.
     * 
     * @param user the current user
     */
    public DoctorHomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        add(new JLabel("Welcome, Dr. " + currentUser.getLName() + "! This is the Doctor Home Panel."), BorderLayout.NORTH);
        // Assuming currentUser is an instance of Doctor or a subclass
        if (currentUser instanceof Doctor) {
            Doctor doctor = (Doctor) currentUser;
            List<Appointment> appointments = doctor.getAppointments(user.getUserID());

            DefaultListModel<Appointment> listModel = new DefaultListModel<>();
            for (Appointment appointment : appointments) {
                listModel.addElement(appointment);
            }
            // Create a JList to display the appointments
            JList<Appointment> appointmentList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(appointmentList);

            add(scrollPane, BorderLayout.CENTER);
        } else {
            // Handle the case where the user is not a Doctor, maybe show an error or different content
            add(new JLabel("User is not a Doctor."), BorderLayout.CENTER);
        }
    }
}
