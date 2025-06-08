package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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

    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchResultPanel;

    public ChatPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Search bar panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        searchButton = new JButton("Search User");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Panel to show search results
        searchResultPanel = new JPanel();
        searchResultPanel.setLayout(new BoxLayout(searchResultPanel, BoxLayout.Y_AXIS));
        add(searchResultPanel, BorderLayout.WEST);
        
        JPanel chatListPanel = StyleConstants.createStyledPanel(new BorderLayout());
        chatListPanel.setBorder(StyleConstants.createTitledBorder("Chat Rooms"));
        // Create a scrollable panel for chat rooms
        JPanel scrollableChatRooms = new JPanel();
        scrollableChatRooms.setLayout(new BoxLayout(scrollableChatRooms, BoxLayout.Y_AXIS));
        
        List<String> chatRoomIDs = DatabaseAccessor.getChatRoomID(currentUser.getUserID());
        //display empty list
        System.out.println("ChatRoomIDs from ChatPanel: " + chatRoomIDs);
        if(chatRoomIDs.isEmpty()) {
            JLabel noChatRoomsLabel = StyleConstants.createLabel("No chat available. " +
                "Start chatting by with others by chatting!", StyleConstants.NORMAL_FONT);
            noChatRoomsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chatListPanel.add(noChatRoomsLabel, BorderLayout.CENTER);
        }else {
            for (int i = 0; i < chatRoomIDs.size(); i++) {
                String chatRoomID = chatRoomIDs.get(i);
                // For each chat room ID, create a ChatRoom object
                System.out.println("ChatRoom from ChatPanel: " + chatRoomID);
                if (chatRoomID == null || chatRoomID.isEmpty()) {
                    continue; // Skip if chat room ID is null or empty
                }
                final ChatRoom chatRoom = new ChatRoom(chatRoomID, currentUser);
                JLabel roomLabel = StyleConstants.createLabel(chatRoom.getChatRoomName(), StyleConstants.NORMAL_FONT);
                roomLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                roomLabel.setOpaque(true);
                roomLabel.setBackground(StyleConstants.LIGHT_BG);
                roomLabel.setForeground(StyleConstants.TEXT_COLOR);
                // Store reference for selection coloring
                roomLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 1) {
                            selectedChatRoom = chatRoom;
                            updateMessagesPanel(selectedChatRoom);
                            // Update label colors
                            for (java.awt.Component comp : scrollableChatRooms.getComponents()) {
                                if (comp instanceof JLabel label) {
                                    if (label == roomLabel) {
                                        label.setBackground(new java.awt.Color(0, 120, 215)); // Blue
                                        label.setForeground(Color.WHITE);
                                    } else {
                                        label.setBackground(StyleConstants.LIGHT_BG);
                                        label.setForeground(StyleConstants.TEXT_COLOR);
                                    }
                                }
                            }
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

        // Add search logic
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchResultPanel.removeAll();
            if (query.isEmpty()) {
                searchResultPanel.add(new JLabel("Enter a username or name to search."));
                searchResultPanel.revalidate();
                searchResultPanel.repaint();
                return;
            }
            // searchResultPanel.removeAll();
            // Search users by username or name (excluding self)
            List<User> foundUsers = DatabaseAccessor.getUsersByNameOrUsername(query);
            if (foundUsers.isEmpty()) {
                searchResultPanel.add(new JLabel("No users found."));
            } else {
                for (User found : foundUsers) {
                    JPanel userPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
                    userPanel.add(new JLabel(found.getFName() + " " + found.getLName() + " (" + found.getUsername() + ")"));
                    JButton chatBtn = new JButton("Chat");
                    chatBtn.addActionListener(ev -> {
                        // Create or get chat room
                        String chatRoomId = com.example.OnlineMedicalAppointment.database.DatabaseAccessor.getChatRoomIdBetweenUsers(currentUser.getUserID(), found.getUserID());
                        // Open the chat room by selecting it
                        ChatRoom newChatRoom = new ChatRoom(chatRoomId, currentUser);
                        selectedChatRoom = newChatRoom;
                        updateMessagesPanel(selectedChatRoom);

                        // Hide search result panel
                        searchResultPanel.setVisible(false);

                        // Add the new chat room to the chat rooms list panel if not already present
                        boolean alreadyExists = false;
                        for (java.awt.Component comp : scrollableChatRooms.getComponents()) {
                            if (comp instanceof JLabel label && label.getText().equals(newChatRoom.getChatRoomName())) {
                                alreadyExists = true;
                                break;
                            }
                        }
                        if (!alreadyExists) {
                            JLabel roomLabel = StyleConstants.createLabel(newChatRoom.getChatRoomName(), StyleConstants.NORMAL_FONT);
                            roomLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                            roomLabel.setOpaque(true);
                            roomLabel.setBackground(StyleConstants.LIGHT_BG);
                            roomLabel.setForeground(StyleConstants.TEXT_COLOR);
                            roomLabel.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getClickCount() == 1) {
                                        selectedChatRoom = newChatRoom;
                                        updateMessagesPanel(selectedChatRoom);
                                        // Update label colors
                                        for (java.awt.Component comp : scrollableChatRooms.getComponents()) {
                                            if (comp instanceof JLabel label) {
                                                if (label == roomLabel) {
                                                    label.setBackground(new java.awt.Color(0, 120, 215)); // Blue
                                                    label.setForeground(Color.WHITE);
                                                } else {
                                                    label.setBackground(StyleConstants.LIGHT_BG);
                                                    label.setForeground(StyleConstants.TEXT_COLOR);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            scrollableChatRooms.add(roomLabel);
                            scrollableChatRooms.revalidate();
                            scrollableChatRooms.repaint();
                        }
                    });
                    userPanel.add(chatBtn);
                    searchResultPanel.add(userPanel);
                }
            }
            searchResultPanel.revalidate();
            searchResultPanel.repaint();
        });
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