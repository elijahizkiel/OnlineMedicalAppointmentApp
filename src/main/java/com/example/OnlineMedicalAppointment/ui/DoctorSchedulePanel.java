package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for displaying and managing a doctor's schedule.
 */
public class DoctorSchedulePanel extends JPanel {

    private User currentUser;

    /**
     * Constructs the DoctorSchedulePanel for the given user.
     * @param user the current user
     */
    public DoctorSchedulePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Doctor Schedule for Dr." + user.getFName());
        add(titleLabel, BorderLayout.NORTH);

        // Create a simple table model for the schedule
        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Object[][] data = {
            {"8:00 AM", "", "", "", "", "", "", ""},
            {"9:00 AM", "", "", "", "", "", "", ""},
            {"10:00 AM", "", "", "", "", "", "", ""},
            {"11:00 AM", "", "", "", "", "", "", ""},
            {"1:00 PM", "", "", "", "", "", "", ""},
            {"2:00 PM", "", "", "", "", "", "", ""},
            {"3:00 PM", "", "", "", "", "", "", ""}
            // Add more time slots as needed
        };

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames);
        javax.swing.JTable scheduleTable = new javax.swing.JTable(model);

        // Add the table to a scroll pane
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(scheduleTable);

        // Add the scroll pane to the center of the panel
        add(scrollPane, BorderLayout.CENTER);

            // Add buttons for loading and saving
            JPanel buttonPanel = new JPanel();
            javax.swing.JButton loadButton = new javax.swing.JButton("Load Schedule");
            javax.swing.JButton saveButton = new javax.swing.JButton("Save Schedule");

            buttonPanel.add(loadButton);
            buttonPanel.add(saveButton);

            add(buttonPanel, BorderLayout.SOUTH);

            // Add action listeners (placeholder logic)
            loadButton.addActionListener(e -> {
                // TODO: Implement logic to load schedule data from a source (e.g., file, database)
                System.out.println("Load button clicked");
                // Example: Call a method to load data into the model
                // loadScheduleData(model);
            });

            saveButton.addActionListener(e -> {
                // TODO: Implement logic to save schedule data to a source (e.g., file, database)
                System.out.println("Save button clicked");
                // Example: Call a method to save data from the model
                // saveScheduleData(model);
            });
    }
}
