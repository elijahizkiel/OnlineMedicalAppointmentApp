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
     * Constructs a new Message instance with the specified parameters.
     * @param id the unique identifier for the message
     * @param senderId the unique identifier of the sender 
     * @param receiverId the unique identifier of the receiver
     * @param roomId the chat room ID
     * @param senderName the sender's name
     * @param receiverName the receiver's name
     * @param message the message content
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

    /**
     * Constructs a new Message instance with timestamp.
     * @param id the message ID
     * @param roomId the chat room ID
     * @param senderId the sender ID
     * @param receiverId the receiver ID
     * @param message the message content
     * @param timestamp the timestamp
     */
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

    /**
     * Constructs a new Message instance with sender and receiver names.
     * @param id the message ID
     * @param roomId the chat room ID
     * @param senderName the sender's name
     * @param receiverName the receiver's name
     * @param message the message content
     */
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

    /**
     * Gets the message ID.
     * @return the message ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the timestamp.
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the room ID.
     * @return the room ID
     */
    public Integer getRoomID(){
        return roomId;
    }

    /**
     * Gets the receiver ID.
     * @return the receiver ID
     */
    public Integer getReceiverId() {
        return receiverId;
    }

    /**
     * Gets the sender ID.
     * @return the sender ID
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * Sets the sender's name.
     * @param senderName the sender's name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Sets the receiver's name.
     * @param receiverName the receiver's name
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * Gets the sender's name.
     * @return the sender's name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Gets the receiver's name.
     * @return the receiver's name
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * Gets the message content.
     * @return the message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content.
     * @param message the message content
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the timestamp.
     * @param timestamp the timestamp
     */
    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;    
    }

    /**
     * Gets the room ID.
     * @return the room ID
     */
    public Integer getRoomId(){
        return roomId;
    }

    /**
     * Returns a string representation of the Message.
     * @return string representation
     */
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

