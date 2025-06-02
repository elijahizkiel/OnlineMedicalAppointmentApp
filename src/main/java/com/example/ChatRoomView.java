package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatRoomView extends JPanel {

    private User loggedInPatient;
    private JTextArea chatArea;
    private JTextField messageInputField;
    private JButton sendButton;
    private GeminiService geminiService;

    public ChatRoomView(User patient) {
        this.loggedInPatient = patient;
        this.geminiService = new GeminiService(); // Initialize the service
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chat Display Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        messageInputField = new JTextField();
        sendButton = new JButton("Send");
        
        inputPanel.add(messageInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Action Listeners
        sendButton.addActionListener(e -> sendMessage());
        messageInputField.addActionListener(e -> sendMessage()); // Allow sending with Enter key
    }

    private void sendMessage() {
        String userMessage = messageInputField.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        appendToChat("You: " + userMessage);
        messageInputField.setText("");

        // Get context from database
        String scheduleContext = DatabaseManager.getUpcomingAppointmentsForContext(loggedInPatient.userID);
        
        // Get response from GeminiService
        // This will run on the EDT, for a real network call, use SwingWorker or similar
        String botResponse = geminiService.getChatResponse(userMessage, scheduleContext);
        appendToChat("Bot: " + botResponse);
    }

    private void appendToChat(String text) {
        chatArea.append(text + "\n");
        // Scroll to the bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
