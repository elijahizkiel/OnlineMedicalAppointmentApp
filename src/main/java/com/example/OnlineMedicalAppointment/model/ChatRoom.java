package com.example.OnlineMedicalAppointment.model;

import java.util.List;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

/**
 * Represents a chat room between two users (typically a Patient and a Doctor).
 * Stores information about the chat room and the users involved.
 */
public class ChatRoom {
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