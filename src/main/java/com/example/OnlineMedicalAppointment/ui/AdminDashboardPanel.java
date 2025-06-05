package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        
        // Main content panel (vertical box)
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(StyleConstants.LIGHT_BG);
        
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
        int adminCount = DatabaseAccessor.getUsersCountByType("Admin");
        int patientCount = DatabaseAccessor.getUsersCountByType("Patient");
        int doctorCount = DatabaseAccessor.getUsersCountByType("Doctor");
        int totalUsers = adminCount + patientCount + doctorCount;
        JPanel userStatsPanel = StyleConstants.createSectionPanel("User Statistics");
        userStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userStatsPanel.add(statLabel("Total Users: ", totalUsers));
        userStatsPanel.add(statLabel("Patients: ", patientCount));
        userStatsPanel.add(statLabel("Doctors: ", doctorCount));
        userStatsPanel.add(statLabel("Admins: ", adminCount));
        
        // Add user type bar chart
        userStatsPanel.add(new UserTypeBarChart(adminCount, doctorCount, patientCount));
        userStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add user registrations line chart
        userStatsPanel.add(new UserRegistrationLineChart());
        userStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add legend for user charts
        userStatsPanel.add(chartLegend(new String[]{"Admins", "Doctors", "Patients", "Registrations"}, new Color[]{Color.ORANGE, Color.BLUE, Color.GREEN, Color.MAGENTA}));
        
        // Appointment statistics section
        JPanel appointmentStatsPanel = StyleConstants.createSectionPanel("Appointment Statistics");
        appointmentStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        appointmentStatsPanel.add(statLabel("Total Appointments: ", allAppointments.size()));
        appointmentStatsPanel.add(statLabel("Appointments Held: ", heldCount));
        appointmentStatsPanel.add(statLabel("Appointments Pending: ", pendingCount));
        appointmentStatsPanel.add(statLabel("Appointments Canceled: ", canceledCount));
        
        // Add appointment status bar chart
        appointmentStatsPanel.add(new AppointmentStatusBarChart(heldCount, pendingCount, canceledCount));
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add appointment status pie chart
        appointmentStatsPanel.add(new AppointmentStatusPieChart(heldCount, pendingCount, canceledCount));
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add legend for appointment charts
        appointmentStatsPanel.add(chartLegend(new String[]{"Held", "Pending", "Canceled"}, new Color[]{Color.GREEN, Color.YELLOW, Color.RED}));
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Add most frequently canceling doctors
        appointmentStatsPanel.add(topCancelingDoctorsPanel());
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add doctors who keep their appointments
        appointmentStatsPanel.add(topHeldDoctorsPanel());
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add daily appointments and held appointments line chart
        appointmentStatsPanel.add(new DailyAppointmentsLineChart());
        appointmentStatsPanel.add(chartLegend(new String[]{"Made", "Held"}, new Color[]{Color.BLUE, Color.GREEN}));
        appointmentStatsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add sections to stats panel with proper alignment
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(userStatsPanel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statsPanel.add(appointmentStatsPanel);
        
        // Add stats panel to main content
        mainContentPanel.add(statsPanel);
        
        // Add some padding at the bottom
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Make scrollable
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
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

    // --- Bar chart for user types ---
    private static class UserTypeBarChart extends JPanel {
        private final int admin, doctor, patient;
        public UserTypeBarChart(int admin, int doctor, int patient) {
            this.admin = admin;
            this.doctor = doctor;
            this.patient = patient;
            setPreferredSize(new Dimension(320, 60));
            setOpaque(false);
            setMaximumSize(new Dimension(320, 60));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int total = admin + doctor + patient;
            if (total == 0) return;
            int[] values = {admin, doctor, patient};
            Color[] colors = {Color.ORANGE, Color.BLUE, Color.GREEN};
            String[] labels = {"Admins", "Doctors", "Patients"};
            int max = Math.max(admin, Math.max(doctor, patient));
            int barWidth = 60, gap = 30, x = 20, y = 30, height = 20;
            for (int i = 0; i < 3; i++) {
                int barLen = (int) (180.0 * values[i] / (max == 0 ? 1 : max));
                g.setColor(colors[i]);
                g.fillRect(x, y, barLen, height);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, barLen, height);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString(labels[i] + ": " + values[i], x, y - 5);
                x += barWidth + gap;
            }
        }
    }

    // --- Bar chart for appointment status ---
    private static class AppointmentStatusBarChart extends JPanel {
        private final long held, pending, canceled;
        public AppointmentStatusBarChart(long held, long pending, long canceled) {
            this.held = held;
            this.pending = pending;
            this.canceled = canceled;
            setPreferredSize(new Dimension(320, 60));
            setOpaque(false);
            setMaximumSize(new Dimension(320, 60));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            long max = Math.max(held, Math.max(pending, canceled));
            long[] values = {held, pending, canceled};
            Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED};
            String[] labels = {"Held", "Pending", "Canceled"};
            int barWidth = 60, gap = 30, x = 20, y = 30, height = 20;
            for (int i = 0; i < 3; i++) {
                int barLen = (int) (180.0 * values[i] / (max == 0 ? 1 : max));
                g.setColor(colors[i]);
                g.fillRect(x, y, barLen, height);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, barLen, height);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString(labels[i] + ": " + values[i], x, y - 5);
                x += barWidth + gap;
            }
        }
    }

    // --- Chart legend panel ---
    private JPanel chartLegend(String[] labels, Color[] colors) {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        for (int i = 0; i < labels.length; i++) {
            JLabel colorBox = new JLabel();
            colorBox.setOpaque(true);
            colorBox.setBackground(colors[i]);
            colorBox.setPreferredSize(new Dimension(16, 16));
            legend.add(colorBox);
            legend.add(new JLabel(labels[i]));
        }
        legend.setOpaque(false);
        return legend;
    }

    // --- Line chart for user registrations over last 12 months ---
    private static class UserRegistrationLineChart extends JPanel {
        private final Map<String, Integer> registrations;
        public UserRegistrationLineChart() {
            this.registrations = DatabaseAccessor.getUserRegistrationsByMonth();
            setPreferredSize(new Dimension(320, 100));
            setOpaque(false);
            setMaximumSize(new Dimension(320, 100));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth() - 40;
            int height = getHeight() - 40;
            int left = 30, top = 20;
            // Get last 12 months
            ArrayList<String> months = new ArrayList<>();
            Calendar cal = new GregorianCalendar();
            for (int i = 11; i >= 0; i--) {
                cal = new GregorianCalendar();
                cal.add(Calendar.MONTH, -i);
                String key = String.format("%d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                months.add(key);
            }
            int max = 1;
            for (String m : months) max = Math.max(max, registrations.getOrDefault(m, 0));
            int prevX = left, prevY = top + height;
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(left, top, width, height);
            for (int i = 0; i < months.size(); i++) {
                int x = left + i * width / (months.size() - 1);
                int val = registrations.getOrDefault(months.get(i), 0);
                int y = top + height - (int) (val * 1.0 / max * height);
                g.setColor(Color.MAGENTA);
                g.fillOval(x - 3, y - 3, 6, 6);
                if (i > 0) {
                    g.drawLine(prevX, prevY, x, y);
                }
                prevX = x;
                prevY = y;
                g.setColor(Color.DARK_GRAY);
                if (i % 2 == 0) g.drawString(months.get(i).substring(2), x - 10, top + height + 15);
            }
            g.setColor(Color.BLACK);
            g.drawString("User Registrations (last 12 months)", left, top - 5);
        }
    }

    // --- Pie chart for appointment status ---
    private static class AppointmentStatusPieChart extends JPanel {
        private final long held, pending, canceled;
        public AppointmentStatusPieChart(long held, long pending, long canceled) {
            this.held = held;
            this.pending = pending;
            this.canceled = canceled;
            setPreferredSize(new Dimension(120, 120));
            setOpaque(false);
            setMaximumSize(new Dimension(120, 120));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            long total = held + pending + canceled;
            if (total == 0) return;
            int startAngle = 0;
            int[] values = {(int) held, (int) pending, (int) canceled};
            Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED};
            for (int i = 0; i < 3; i++) {
                int angle = (int) Math.round(values[i] * 360.0 / total);
                g.setColor(colors[i]);
                g.fillArc(10, 10, 100, 100, startAngle, angle);
                startAngle += angle;
            }
            g.setColor(Color.BLACK);
            g.drawString("Appointment Status", 10, 125);
        }
    }

    // --- Panel for most frequently appointment-canceling doctors ---
    private JPanel topCancelingDoctorsPanel() {
        JPanel panel = StyleConstants.createSectionPanel("Top Canceling Doctors");
        List<Object[]> data = DatabaseAccessor.getTopDoctorsByAppointmentStatus("Canceled", 5);
        for (Object[] row : data) {
            String name = (String) row[0];
            long count = (long) row[1];
            panel.add(statLabel(name + " (Canceled): ", count));
        }
        if (data.isEmpty()) {
            panel.add(statLabel("No data", 0));
        }
        return panel;
    }

    // --- Panel for doctors who keep their appointments ---
    private JPanel topHeldDoctorsPanel() {
        JPanel panel = StyleConstants.createSectionPanel("Top Reliable Doctors");
        List<Object[]> data = DatabaseAccessor.getTopDoctorsByAppointmentStatus("Held", 5);
        for (Object[] row : data) {
            String name = (String) row[0];
            long count = (long) row[1];
            panel.add(statLabel(name + " (Held): ", count));
        }
        if (data.isEmpty()) {
            panel.add(statLabel("No data", 0));
        }
        return panel;
    }

    // --- Line chart for daily appointments made and held ---
    private static class DailyAppointmentsLineChart extends JPanel {
        private final Map<String, Integer> made, held;
        public DailyAppointmentsLineChart() {
            this.made = DatabaseAccessor.getAppointmentsMadeByDay(14);
            this.held = DatabaseAccessor.getAppointmentsHeldByDay(14);
            setPreferredSize(new Dimension(320, 100));
            setOpaque(false);
            setMaximumSize(new Dimension(320, 100));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth() - 40;
            int height = getHeight() - 40;
            int left = 30, top = 20;
            // Get last 14 days
            ArrayList<String> days = new ArrayList<>();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            for (int i = 13; i >= 0; i--) {
                cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_MONTH, -i);
                days.add(sdf.format(cal.getTime()));
            }
            int max = 1;
            for (String d : days) {
                max = Math.max(max, made.getOrDefault(d, 0));
                max = Math.max(max, held.getOrDefault(d, 0));
            }
            // Draw axes
            g.setColor(java.awt.Color.LIGHT_GRAY);
            g.drawRect(left, top, width, height);
            // Draw lines
            int prevX = left, prevYMade = top + height, prevYHeld = top + height;
            for (int i = 0; i < days.size(); i++) {
                int x = left + i * width / (days.size() - 1);
                int yMade = top + height - (int) (made.getOrDefault(days.get(i), 0) * 1.0 / max * height);
                int yHeld = top + height - (int) (held.getOrDefault(days.get(i), 0) * 1.0 / max * height);
                // Made line (blue)
                g.setColor(java.awt.Color.BLUE);
                g.fillOval(x - 2, yMade - 2, 4, 4);
                if (i > 0) g.drawLine(prevX, prevYMade, x, yMade);
                prevYMade = yMade;
                // Held line (green)
                g.setColor(java.awt.Color.GREEN);
                g.fillOval(x - 2, yHeld - 2, 4, 4);
                if (i > 0) g.drawLine(prevX, prevYHeld, x, yHeld);
                prevYHeld = yHeld;
                prevX = x;
                // X-axis labels
                g.setColor(java.awt.Color.DARK_GRAY);
                if (i % 2 == 0) g.drawString(days.get(i).substring(5), x - 10, top + height + 15);
            }
            g.setColor(java.awt.Color.BLACK);
            g.drawString("Appointments Made & Held (last 14 days)", left, top - 5);
        }
    }
}
