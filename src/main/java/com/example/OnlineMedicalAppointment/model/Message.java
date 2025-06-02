package com.example.OnlineMedicalAppointment.model;

import java.sql.Timestamp;
/**
 * Represents a message in the online medical appointment system.
 * This class encapsulates the details of a message including sender and receiver information,
 * the content of the message, and the timestamp when the message was created.
 */
public class Message {
    private final Integer id;
    private final Integer senderId;
    private final Integer receiverId;
    private final Integer roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private Timestamp timestamp;

    /**
     *
     * Constructs a new Message instance with the specified parameters.
     * @param id the unique identifier for the message
     * @param senderId the unique identifier of the sender 
     */
        public Message(Integer id, 
                Integer senderId, 
                Integer receiverId, 
                Integer roomId,
                String senderName, 
                String receiverName, 
                String message) {
            this.id = id;
            this.roomId = roomId;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.message = message;
            this.timestamp = new Timestamp(System.currentTimeMillis());
        }

        public Message(Integer id, 
        Integer roomId,
        Integer senderId, 
        Integer receiverId, 
        String message,
        Timestamp timestamp) {
            this.id = id;
            this.roomId = roomId;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.message = message;
            this.timestamp = timestamp;
        }

        public Message(Integer id, Integer roomId, String senderName, String receiverName, String message) {
            this.id = id;
            this.roomId = roomId;
            this.senderId = null; // senderId is missing
            this.receiverId = null; // receiverId is missing
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.message = message;
            this.timestamp = new Timestamp(System.currentTimeMillis());
        }

    public Integer getId() {
        return id;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public Integer getRoomID(){
        return roomId;
    }
    public Integer getReceiverId() {
        return receiverId;
    }

    public Integer getSenderId() {
        return senderId;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;    
    }

    public Integer getRoomId(){
        return roomId;
    }
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", message='" + message + '\'' +
                ", time=" + timestamp +
                '}';
    }
}

