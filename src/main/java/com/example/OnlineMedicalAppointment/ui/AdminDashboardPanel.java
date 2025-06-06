package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.Appointment;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for displaying the admin dashboard.
 */
public class AdminDashboardPanel extends JPanel {

    private final User currentUser;

    /**
     * Constructs the AdminDashboardPanel for the given user.
     * 
     * @param user the current user
     */

    public AdminDashboardPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.LIGHT_BG);
        
        // Title panel
        JLabel titleLabel = StyleConstants.createLabel("Admin Dashboard - Statistics", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainContentPanel = StyleConstants.createStyledPanel(new BorderLayout());
        
        // Statistics panel with grid layout
        JPanel statsPanel = StyleConstants.createStyledPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Get appointment statistics
        List<Appointment> allAppointments = DatabaseAccessor.getAllAppointments();
        long heldCount = allAppointments.stream().filter(a -> "Held".equalsIgnoreCase(a.getStatus())).count();
        long canceledCount = allAppointments.stream().filter(a -> "Canceled".equalsIgnoreCase(a.getStatus())).count();
        long pendingCount = allAppointments.stream().filter(a -> "Pending".equalsIgnoreCase(a.getStatus())).count();
        
        // User statistics section
        JPanel userStatsPanel = StyleConstants.createSectionPanel("User Statistics");
        userStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userStatsPanel.add(statLabel("Total Users: ", DatabaseAccessor.getUsersCountByType("Admin") + 
            DatabaseAccessor.getUsersCountByType("Patient") + DatabaseAccessor.getUsersCountByType("Doctor")));
        userStatsPanel.add(statLabel("Patients: ", DatabaseAccessor.getUsersCountByType("Patient")));
        userStatsPanel.add(statLabel("Doctors: ", DatabaseAccessor.getUsersCountByType("Doctor")));
        userStatsPanel.add(statLabel("Admins: ", DatabaseAccessor.getUsersCountByType("Admin")));
        
        // Appointment statistics section
        JPanel appointmentStatsPanel = StyleConstants.createSectionPanel("Appointment Statistics");
        appointmentStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        appointmentStatsPanel.add(statLabel("Total Appointments: ", allAppointments.size()));
        appointmentStatsPanel.add(statLabel("Appointments Held: ", heldCount));
        appointmentStatsPanel.add(statLabel("Appointments Pending: ", pendingCount));
        appointmentStatsPanel.add(statLabel("Appointments Canceled: ", canceledCount));
        
        // Add sections to stats panel with proper alignment
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(userStatsPanel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statsPanel.add(appointmentStatsPanel);
        
        // Add stats panel to main content
        JPanel statsWrapper = StyleConstants.createStyledPanel(new BorderLayout());
        statsWrapper.setBackground(StyleConstants.LIGHT_BG);
        statsWrapper.setBorder(BorderFactory.createEmptyBorder(0, 80, 40, 80));
        
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(StyleConstants.LIGHT_BG);
        centerWrapper.add(statsPanel, BorderLayout.NORTH);
        
        statsWrapper.add(centerWrapper, BorderLayout.CENTER);
        mainContentPanel.add(statsWrapper, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Add some padding at the bottom
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);
    }

    private JPanel statLabel(String label, long value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JLabel l = StyleConstants.createLabel(label, StyleConstants.NORMAL_FONT);
        JLabel v = StyleConstants.createLabel(String.valueOf(value), StyleConstants.NORMAL_FONT.deriveFont(java.awt.Font.BOLD));
        v.setForeground(StyleConstants.PRIMARY_COLOR);
        panel.add(l);
        panel.add(v);
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 30));
        return panel;
    }
}
