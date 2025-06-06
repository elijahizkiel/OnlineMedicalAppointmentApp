package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.model.GeminiClient;
import com.example.OnlineMedicalAppointment.model.User;

public class GeminiChatPanel extends JPanel {
    private final GeminiClient geminiClient;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    
    public GeminiChatPanel(User user) {
        this.geminiClient = new GeminiClient();
        setLayout(new BorderLayout());
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Title
        JLabel titleLabel = StyleConstants.createLabel("Gemini AI Assistant", StyleConstants.TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Chat display area
        chatArea = new JTextArea(15, 40);
        chatArea.setEditable(false);
        chatArea.setFont(StyleConstants.NORMAL_FONT);
        chatArea.setBackground(StyleConstants.LIGHT_BG);
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputField = new JTextField();
        inputField.setFont(StyleConstants.NORMAL_FONT);
        
        sendButton = new JButton("Send");
        sendButton.setFont(StyleConstants.NORMAL_FONT);
        sendButton.addActionListener(e -> sendMessage());
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) return;
        
        // Add user message (right aligned)
        appendMessage("You: " + userMessage, true);
        inputField.setText("");
        
        // Get AI response
        String aiResponse = geminiClient.generateContent(userMessage);
        
        // Add AI response (left aligned)
        appendMessage("ChatBot: " + aiResponse, false);
    }
    
    private void appendMessage(String message, boolean isUser) {
        // Save current text
        String currentText = chatArea.getText();
        
        // Clear and re-add with proper alignment
        chatArea.setText("");
        
        // Add existing messages
        chatArea.append(currentText);
        
        // Add new message with alignment
        if (isUser) {
            // Right align user messages
            chatArea.append("\n" + message + "\n");
        } else {
            // Left align AI messages
            chatArea.append("\n" + message + "\n");
        }
        
        // Scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
