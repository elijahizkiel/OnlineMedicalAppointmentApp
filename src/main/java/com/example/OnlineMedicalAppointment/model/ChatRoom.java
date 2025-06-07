package com.example.OnlineMedicalAppointment.model;

import java.util.List;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

/**
 * Represents a chat room between two users (typically a Patient and a Doctor).
 * Stores information about the chat room and the users involved.
 */
public class ChatRoom {
    private String chatRoomName;
    private String chatRoomID;
    private User chattingWith;
    
    public ChatRoom(String chatRoomID, User currentUser) {
        this.chatRoomID = chatRoomID;
        if (chatRoomID == null || chatRoomID.trim().isEmpty() || !chatRoomID.contains("-")) {
            // Defensive: invalid chatRoomID format, avoid NumberFormatException
            chattingWith = null;
            chatRoomName = "Unknown User";
            return;
        }
        String[] users = chatRoomID.split("-");
        if (users.length != 2) {
            chattingWith = null;
            chatRoomName = "Unknown User";
            return;
        }
        try {
            int userId0 = Integer.parseInt(users[0]);
            int userId1 = Integer.parseInt(users[1]);
            if (currentUser.getUserID() == userId0) {
                chattingWith = DatabaseAccessor.getUserByID(userId1);
            } else {
                chattingWith = DatabaseAccessor.getUserByID(userId0);
            }
            if (chattingWith != null) {
                chatRoomName = chattingWith.getFName() + " " + chattingWith.getLName();
            } else {
                chatRoomName = "Unknown User";
            }
        } catch (NumberFormatException e) {
            chattingWith = null;
            chatRoomName = "Unknown User";
        }
    } 
    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public User getChattingWith() {
        return chattingWith;
    }
    public void setChattingWith(User chattingWith) {
        this.chattingWith = chattingWith;
    }
    public List<Message> getMessages() {
        List<Message> messages = DatabaseAccessor.getMessages(chatRoomID);
        System.out.println("ChatRoom.getMessages: " + messages);
        return messages;
    }
}