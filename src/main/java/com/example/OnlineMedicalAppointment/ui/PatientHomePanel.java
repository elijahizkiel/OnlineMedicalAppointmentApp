package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

public class PatientHomePanel extends JPanel {

    private final User currentUser;

    // Style constants
    public static final Color LIGHT_BG = new Color(240, 240, 240);
    public static final Color TEXT_COLOR = new Color(50, 50, 50);
    public static final Color SECONDARY_COLOR = new Color(150, 150, 150);
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);

    public static JPanel createStyledPanel(BorderLayout layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(LIGHT_BG);
        return panel;
    }

    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public PatientHomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a main content panel with padding
        JPanel contentPanel = createStyledPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Welcome title
        JLabel titleLabel = new JLabel("Welcome, " + currentUser.getFName() + "!");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Create cards panel with grid layout
        JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        cardsPanel.setOpaque(false);

        // Appointment card
        JPanel appointmentCard = createCard("Upcoming Appointments", "View and manage your appointments", "calendar-icon");
        cardsPanel.add(appointmentCard);

        // Doctors card
        JPanel doctorsCard = createCard("Find Doctors", "Search and book appointments with specialists", "doctor-icon");
        cardsPanel.add(doctorsCard);

        // Medical Records card
        JPanel recordsCard = createCard("Medical Records", "Access your health history and reports", "records-icon");
        cardsPanel.add(recordsCard);

        // Messages card
        JPanel messagesCard = createCard("Messages", "Communicate with your healthcare providers", "messages-icon");
        cardsPanel.add(messagesCard);

        contentPanel.add(cardsPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Show appointment status
        JLabel appointmentStatusLabel;
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        if (appointments != null && !appointments.isEmpty()) {
            appointmentStatusLabel = new JLabel("You have upcoming appointments.");
        } else {
            appointmentStatusLabel = new JLabel("You have no upcoming appointments.");
        }
        add(appointmentStatusLabel, BorderLayout.SOUTH);

        // Available Doctors
        List<Doctor> sampleDoctors = DatabaseAccessor.getDoctors();
        if (sampleDoctors != null && !sampleDoctors.isEmpty()) {
            for (Doctor doctor : sampleDoctors) {
                JLabel doctorLabel = new JLabel(
                    "Dr. " + doctor.getFName() + " - " + doctor.getSpecialty() + " - Hours: " + "08:00AM - 05:30PM" );
                doctorLabel.setAlignmentX(LEFT_ALIGNMENT); // Align left
                add(doctorLabel, BorderLayout.SOUTH);
            }
        } else {
            JLabel noDoctorsLabel = new JLabel("No sample doctors available at the moment.");
            add(noDoctorsLabel, BorderLayout.SOUTH);
        }
    }

    // Helper method to create cards
    private JPanel createCard(String title, String description, String iconName) {
        JPanel card = createStyledPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(new ImageIcon("icons/" + iconName + ".png"));
        JLabel titleLabel = createLabel(title, SUBTITLE_FONT);
        JLabel descLabel = createLabel(description, NORMAL_FONT);
        descLabel.setForeground(SECONDARY_COLOR);
        
        JPanel textPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
}
