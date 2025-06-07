package com.example.OnlineMedicalAppointment.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Message class.
 */
class MessageTest {
    /**
     * Tests the fields and getters of the Message class.
     */
    @Test
    void testMessageFields() {
        Message msg = new Message(1, 2, 3, "2-3", "sender", "receiver","Hello");
        assertEquals(1, msg.getId());
        assertEquals(2, msg.getSenderId());
        assertEquals(3, msg.getReceiverId());
        assertEquals("Hello", msg.getMessage());
        assertEquals("sender", msg.getSenderName());
        assertEquals("receiver", msg.getReceiverName());
    }
}
