package com.example.OnlineMedicalAppointment.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;
import com.example.OnlineMedicalAppointment.model.User;
import com.example.OnlineMedicalAppointment.model.Message;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
// import com.example.OnlineMedicalAppointment.model.ChatRoom;

public class PatientChatPanel extends JPanel {

    private final User currentUser;

    public PatientChatPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JPanel chatListPanel = new JPanel(); // Placeholder for chat room list
        chatListPanel.add(new JLabel("Chat Rooms List"));
        DatabaseAccessor.getChatRoomID(currentUser.getUserID()).forEach(chatRoomID -> {
            ChatRoom chatRoom = new ChatRoom(chatRoomID, currentUser);
            chatListPanel.add(new JLabel(chatRoom.getChatRoomName())); 
        });

        JPanel messagePanel = new JPanel(); // Placeholder for message display
        messagePanel.add(new JLabel("Messages")); // Example content

        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, chatListPanel, messagePanel);
        splitPane.setResizeWeight(0.3); // Give 30% to the left side (chat list)

        add(splitPane, BorderLayout.CENTER);
    }
}

class ChatRoom {
    private String chatRoomName;
    private int chatRoomID;
    private User chattingWith;
    private List<Message> messages;
    
    public ChatRoom(String chatRoomID, User currentUser) {

        String [] users = chatRoomID.split("-");
            if(currentUser.getUserID() == Integer.parseInt(users[0])) {
                User messagingWith = DatabaseAccessor.getUserByID(Integer.parseInt(users[1]));
                chatRoomName = messagingWith.getFName() + " " + messagingWith.getLName();
            } else {
                User messagingWith = DatabaseAccessor.getUserByID(Integer.parseInt(users[0]));
                chatRoomName = messagingWith.getFName() + " " + messagingWith.getLName(); 
            }
            messages =  DatabaseAccessor.getMessages(chatRoomID);
    } 
    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public int getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(int chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public User getChattingWith() {
        return chattingWith;
    }
    public void setChattingWith(User chattingWith) {
        this.chattingWith = chattingWith;
    }
    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}