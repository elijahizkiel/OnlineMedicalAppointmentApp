package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.Doctor;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for displaying the patient's home view.
 * Shows a welcome message, quick access cards, and upcoming appointments/doctors.
 */
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

        // Main vertical content panel for all content
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new javax.swing.BoxLayout(verticalPanel, javax.swing.BoxLayout.Y_AXIS));
        verticalPanel.setBackground(LIGHT_BG);
        verticalPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Welcome title
        JLabel titleLabel = new JLabel("Welcome, " + currentUser.getFName() + "!");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        verticalPanel.add(titleLabel);

        // Cards panel
        JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        cardsPanel.setOpaque(false);

        JPanel appointmentCard = createCard("Upcoming Appointments", "View and manage your appointments", "calendar-icon");
        appointmentCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        appointmentCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToTab("Booking");
            }
        });
        cardsPanel.add(appointmentCard);

        JPanel doctorsCard = createCard("Find Doctors", "Search and book appointments with specialists", "doctor-icon");
        doctorsCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        doctorsCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToTab("Booking");
            }
        });
        cardsPanel.add(doctorsCard);

        JPanel messagesCard = createCard("Messages", "Communicate with your healthcare providers", "messages-icon");
        messagesCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        messagesCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToTab("Chat-Room");
            }
        });
        cardsPanel.add(messagesCard);

        cardsPanel.setAlignmentX(LEFT_ALIGNMENT);
        verticalPanel.add(cardsPanel);

        // Appointment status
        List<Appointment> appointments = DatabaseAccessor.getAppointments(currentUser.getUserID());
        JLabel appointmentStatusLabel;
        if (appointments != null && !appointments.isEmpty()) {
            appointmentStatusLabel = new JLabel("You have upcoming appointments.");
        } else {
            appointmentStatusLabel = new JLabel("You have no upcoming appointments.");
        }
        appointmentStatusLabel.setFont(NORMAL_FONT);
        appointmentStatusLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        appointmentStatusLabel.setAlignmentX(LEFT_ALIGNMENT);
        verticalPanel.add(appointmentStatusLabel);

        // Available Doctors
        List<Doctor> sampleDoctors = DatabaseAccessor.getDoctors();
        if (sampleDoctors != null && !sampleDoctors.isEmpty()) {
            for (Doctor doctor : sampleDoctors) {
                JLabel doctorLabel = new JLabel(
                    "Dr. " + doctor.getFName() + " - " + doctor.getSpecialty() + " - Hours: 08:00AM - 05:30PM");
                doctorLabel.setFont(NORMAL_FONT);
                doctorLabel.setAlignmentX(LEFT_ALIGNMENT);
                verticalPanel.add(doctorLabel);
            }
        } else {
            JLabel noDoctorsLabel = new JLabel("No sample doctors available at the moment.");
            noDoctorsLabel.setFont(NORMAL_FONT);
            noDoctorsLabel.setAlignmentX(LEFT_ALIGNMENT);
            verticalPanel.add(noDoctorsLabel);
        }

        // Wrap verticalPanel in a scroll pane for full scrollability
        JScrollPane scrollPane = new JScrollPane(verticalPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
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

    /**
     * Helper to redirect to a tab in the main app frame by tab title.
     */
    private void redirectToTab(String tabTitle) {
        java.awt.Component c = SwingUtilities.getWindowAncestor(this);
        if (c instanceof javax.swing.JFrame frame) {
            if (frame instanceof MainAppFrame mainFrame) {
                javax.swing.JTabbedPane tabs = (javax.swing.JTabbedPane) mainFrame.getContentPane().getComponent(0);
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (tabs.getTitleAt(i).equals(tabTitle)) {
                        tabs.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
}
