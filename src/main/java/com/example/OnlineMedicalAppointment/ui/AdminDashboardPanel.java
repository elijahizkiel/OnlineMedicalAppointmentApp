package com.example.OnlineMedicalAppointment.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Admin Dashboard - Statistics");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 15)));

        List<Appointment> allAppointments = DatabaseAccessor.getAllAppointments();
        long heldCount = allAppointments.stream().filter(a -> "Held".equalsIgnoreCase(a.getStatus())).count();
        long canceledCount = allAppointments.stream().filter(a -> "Canceled".equalsIgnoreCase(a.getStatus())).count();
        long pendingCount = allAppointments.stream().filter(a -> "Pending".equalsIgnoreCase(a.getStatus())).count();

        // Statistics labels
        add(statLabel("Total Users: ", DatabaseAccessor.getUsersCountByType("Admin")+ 
            DatabaseAccessor.getUsersCountByType("Patient") +
            DatabaseAccessor.getUsersCountByType("Doctor")));
        add(statLabel("Patients: ", DatabaseAccessor.getUsersCountByType("Patient")));
        add(statLabel("Doctors: ", DatabaseAccessor.getUsersCountByType("Doctor")));
        add(statLabel("Admins: ", DatabaseAccessor.getUsersCountByType("Admins")));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(statLabel("Total Appointments: ", allAppointments.size()));
        add(statLabel("Appointments Held: ", heldCount));
        add(statLabel("Appointments Pending: ", pendingCount));
        add(statLabel("Appointments Canceled: ", canceledCount));
        add(Box.createVerticalGlue());
    }

    private JPanel statLabel(String label, long value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel v = new JLabel(String.valueOf(value));
        v.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(l);
        panel.add(v);
        panel.setOpaque(false);
        return panel;
    }
}
