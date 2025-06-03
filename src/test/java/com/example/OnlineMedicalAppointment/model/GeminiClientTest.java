package com.example.OnlineMedicalAppointment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeminiClientTest {

    @Test
    void testStartChat_succeeds() {
        GeminiClient client = new GeminiClient("TEST_API_KEY");

        GeminiClient.ChatSession chatSession = assertDoesNotThrow(() -> {
            return client.startChat();
        }, "startChat should not throw an exception");

        assertNotNull(chatSession, "ChatSession should not be null");
    }
}
