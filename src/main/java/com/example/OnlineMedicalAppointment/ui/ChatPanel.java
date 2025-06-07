package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.ChatRoom;
import com.example.OnlineMedicalAppointment.model.Message;
import com.example.OnlineMedicalAppointment.model.User;

/**
 * Panel for patient chat functionality.
 * Allows patients to view chat rooms and send messages to doctors.
 * Note: This panel is superseded by the generic ChatPanel.
 */
public class ChatPanel extends JPanel {

    private final User currentUser;
    private ChatRoom selectedChatRoom = null;
    private final JTextArea messagesDisplayArea;
    private final JTextField messageInputField;

    public ChatPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Title
        JLabel titleLabel = StyleConstants.createLabel("Chat with Doctors", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel chatListPanel = StyleConstants.createStyledPanel(new BorderLayout());
        chatListPanel.setBorder(StyleConstants.createTitledBorder("Chat Rooms"));
        // Create a scrollable panel for chat rooms
        JPanel scrollableChatRooms = new JPanel();
        scrollableChatRooms.setLayout(new BoxLayout(scrollableChatRooms, BoxLayout.Y_AXIS));
        
        List<String> chatRoomIDs = DatabaseAccessor.getChatRoomID(currentUser.getUserID());
        //display empty list
        if(chatRoomIDs.isEmpty()) {
            JLabel noChatRoomsLabel = StyleConstants.createLabel("No chat available. " +
                "Start chatting by with others by chatting!", StyleConstants.NORMAL_FONT);
            noChatRoomsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chatListPanel.add(noChatRoomsLabel, BorderLayout.CENTER);
        }else {
            for ( int i = 0;i < chatRoomIDs.size(); i++) {
            String chatRoomID = chatRoomIDs.get(i);
            // For each chat room ID, create a ChatRoom object
            System.out.println("ChatRoom from ChatPanel: " + chatRoomID);
            if (chatRoomID == null || chatRoomID.isEmpty()) {
                continue; // Skip if chat room ID is null or empty
            }
            final ChatRoom chatRoom = new ChatRoom(chatRoomID, currentUser);
            // Create a label for each chat room
            JLabel roomLabel = StyleConstants.createLabel(chatRoom.getChatRoomName(), StyleConstants.NORMAL_FONT);
            roomLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Add mouse listener to handle chat room selection
            roomLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        selectedChatRoom = chatRoom;
                        updateMessagesPanel(selectedChatRoom);
                    }
                }
            });
            scrollableChatRooms.add(roomLabel);
        }
    }
        
        JScrollPane chatRoomsScrollPane = new JScrollPane(scrollableChatRooms);
        chatRoomsScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatListPanel.add(chatRoomsScrollPane, BorderLayout.CENTER);

        JPanel messagePanel = StyleConstants.createStyledPanel(new BorderLayout());
        messagePanel.setBorder(StyleConstants.createTitledBorder("Messages"));
        JLabel messagesTitle = StyleConstants.createLabel("Messages", StyleConstants.SUBTITLE_FONT);
        messagesTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messagePanel.add(messagesTitle, BorderLayout.NORTH);

        // Create scrollable text area for displaying messages
        messagesDisplayArea = new JTextArea(10, 30);
        messagesDisplayArea.setEditable(false);
        messagesDisplayArea.setBackground(StyleConstants.LIGHT_BG);
        messagesDisplayArea.setFont(StyleConstants.NORMAL_FONT);
        
        JScrollPane messagesScrollPane = new JScrollPane(messagesDisplayArea);
        messagesScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messagePanel.add(messagesScrollPane, BorderLayout.CENTER);

        // Create input panel for message input and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create text field for writing messages
        messageInputField = new JTextField(30);
        messageInputField.setFont(StyleConstants.NORMAL_FONT);
        messageInputField.setBorder(BorderFactory.createTitledBorder("Write a message"));
        inputPanel.add(messageInputField, BorderLayout.CENTER);

        // Create send button
        JButton sendButton = new JButton("Send");
        sendButton.setFont(StyleConstants.NORMAL_FONT);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        messagePanel.add(inputPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatListPanel, messagePanel);
        splitPane.setResizeWeight(0.3); // Give 30% to the left side (chat list)
        splitPane.setBackground(StyleConstants.SECONDARY_COLOR);
        splitPane.setDividerSize(2);

        // Add main content with padding
        JPanel wrapperPanel = StyleConstants.createStyledPanel(new BorderLayout());
        wrapperPanel.add(splitPane, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private void updateMessagesPanel(ChatRoom chatRoom) {
        messagesDisplayArea.setText("");
        
        List<Message> messages = chatRoom.getMessages();
        for (Message message : messages) {
            String messageText = String.format("%s: %s\n", 
                message.getSenderName(), 
                message.getMessage());
            messagesDisplayArea.append(messageText);
        }
    }

    private void sendMessage() {
        if (selectedChatRoom == null) {
            JOptionPane.showMessageDialog(this, "Please select a chat room first.", "No Chat Room Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String messageText = messageInputField.getText().trim();
        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message.", "Empty Message", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create and send the message
        Message newMessage = new Message(
            null, // id will be set by database
            currentUser.getUserID(), 
            selectedChatRoom.getChattingWith().getUserID(), 
            String.valueOf(selectedChatRoom.getChatRoomID()),
            currentUser.getFName() + " " + currentUser.getLName(),
            selectedChatRoom.getChattingWith().getFName() + " " + selectedChatRoom.getChattingWith().getLName(),
            messageText
        );
        
        if (DatabaseAccessor.addMessage(newMessage)) {
            messageInputField.setText(""); // Clear the input field
            updateMessagesPanel(selectedChatRoom); // Refresh messages
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send message. Please try again.", "Send Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}