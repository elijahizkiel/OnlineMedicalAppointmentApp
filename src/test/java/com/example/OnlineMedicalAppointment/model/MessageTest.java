package com.example.OnlineMedicalAppointment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testMessageFields() {
        Message msg = new Message(1, 2, 3, "Hello", "sender", "receiver");
        assertEquals(1, msg.getId());
        assertEquals(2, msg.getSenderId());
        assertEquals(3, msg.getReceiverId());
        assertEquals("Hello", msg.getMessage());
        assertEquals("sender", msg.getSenderName());
        assertEquals("receiver", msg.getReceiverName());
    }
}
