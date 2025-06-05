package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
import com.example.OnlineMedicalAppointment.model.ChatRoom;
import com.example.OnlineMedicalAppointment.model.Message;
import com.example.OnlineMedicalAppointment.ui.StyleConstants;

public class DoctorChatPanel extends JPanel {

    private final User currentUser;
    private JTextArea messagesDisplayArea;
    private JTextField messageInputField;
    private ChatRoom selectedChatRoom = null;

    public DoctorChatPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(StyleConstants.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        // Title
        JLabel titleLabel = StyleConstants.createLabel("Chat with Patients", StyleConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel chatListPanel = StyleConstants.createStyledPanel(new BorderLayout());
        chatListPanel.setBorder(StyleConstants.createTitledBorder("Chat Rooms"));
        JLabel chatListTitle = StyleConstants.createLabel("Chat Rooms List", StyleConstants.SUBTITLE_FONT);
        chatListTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chatListPanel.add(chatListTitle, BorderLayout.NORTH);
        
        // Create a scrollable panel for chat rooms
        JPanel scrollableChatRooms = new JPanel();
        scrollableChatRooms.setLayout(new BoxLayout(scrollableChatRooms, BoxLayout.Y_AXIS));
        
        List<String> chatRoomIDs = DatabaseAccessor.getChatRoomID(currentUser.getUserID());
        
        for (String chatRoomID : chatRoomIDs) {
            final ChatRoom chatRoom = new ChatRoom(chatRoomID, currentUser);
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

        // Create text field for writing messages
        messageInputField = new JTextField(30);
        messageInputField.setFont(StyleConstants.NORMAL_FONT);
        messageInputField.setBorder(BorderFactory.createTitledBorder("Write a message"));
        messagePanel.add(messageInputField, BorderLayout.SOUTH);

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
}
