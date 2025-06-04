package com.example.OnlineMedicalAppointment.model;

import com.google.genai.types.*; // Wildcard for Content, Part, GenerateContentResponse, GenerateContentConfig, Candidate
import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
// import org.mockito.MockitoAnnotations; // Not strictly needed with MockitoExtension
import org.mockito.junit.jupiter.MockitoExtension;

// import java.lang.reflect.Field; // Not needed as getConfig() is used
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeminiClientTest {

    @Mock
    private GeminiClient.GenerativeModelAdapter mockAdapter;

    private GeminiClient clientWithMockAdapter;
    private GenerateContentConfig actualConfigForSession; 

    @BeforeEach
    void setUp() throws Exception {
        clientWithMockAdapter = new GeminiClient(mockAdapter);

        // To get a realistic GenerateContentConfig for tests, we can instantiate a real GeminiClient.
        // This requires GOOGLE_API_KEY to be set in the environment for this setup part.
        GeminiClient realClientForConfig = new GeminiClient("DUMMY_API_KEY_FOR_CONFIG");
        GeminiClient.ChatSession tempSession = realClientForConfig.startChat();
        actualConfigForSession = tempSession.getConfig(); 
    }

    @Test
    void testStartChat_succeeds() {
        // Using the clientWithMockAdapter to avoid GOOGLE_API_KEY dependency for this test's core logic
        // The real test of startChat's full functionality (if it made external calls beyond config)
        // would be more of an integration test or need deeper mocking of the Client itself.
        // Here, we primarily test that it produces a non-null ChatSession and config.
        GeminiClient.ChatSession session = clientWithMockAdapter.startChat();
        assertNotNull(session, "ChatSession should not be null when using mock adapter.");
        assertNotNull(session.getConfig(), "ChatSession's config should not be null.");
        // Optionally check if the config created by startChat (which uses the mockAdapter path)
        // is similar in structure to the one we got from a real client, if that's relevant.
        // For this test, ensuring it doesn't throw and creates a session is key.
        // The tools list in the config from clientWithMockAdapter.startChat() will be based on
        // the actual DatabaseFunctions.class.getMethod("getProductPrice", String.class) call.
        assertEquals(actualConfigForSession.getTools().size(), session.getConfig().getTools().size()); // Changed to getTools()
        if (!actualConfigForSession.getTools().isEmpty() && !session.getConfig().getTools().isEmpty()) { // Changed to getTools()
            assertEquals(
                actualConfigForSession.getTools().get(0).getFunctionDeclarationsList().get(0).getName(),
                session.getConfig().getTools().get(0).getFunctionDeclarationsList().get(0).getName() // Changed to getTools()
            );
        }
    }

    @Test
    void testSendMessage_succeeds() throws Exception {
        // ChatSession is now obtained from the clientWithMockAdapter, which uses the mockAdapter
        GeminiClient.ChatSession chatSession = clientWithMockAdapter.startChat();
        // The config within this chatSession was created by clientWithMockAdapter.startChat()
        GenerateContentConfig sessionConfig = chatSession.getConfig();

        String expectedResponseText = "Mocked response from Gemini.";
        Part responsePart = Part.fromText(expectedResponseText); // Changed to Part.fromText()
        Content responseContent = Content.newBuilder().addPart(responsePart).setRole("model").build(); // Changed to addPart()
        Candidate candidate = Candidate.builder().setContent(responseContent).build(); // Changed to builder()
        GenerateContentResponse mockApiResponse = GenerateContentResponse.builder().addCandidates(candidate).build(); // Changed to builder()

        // Configure the mockAdapter for the generateContent call
        when(mockAdapter.generateContent(
            eq("gemini-pro"),       // model name
            anyList(),              // List<Content> for history + new message
            eq(sessionConfig)       // The GenerateContentConfig used by this specific ChatSession
        )).thenReturn(mockApiResponse);

        String userMessage = "Hello from test";
        String actualResponse = chatSession.sendMessage(userMessage);

        assertEquals(expectedResponseText, actualResponse, "Response should match mocked response.");

        List<Content> finalHistory = chatSession.getHistory(); 
        assertEquals(2, finalHistory.size(), "History should have user and assistant messages.");
        assertTrue(finalHistory.get(0).getPartsList().get(0).getText().contains(userMessage), "User message missing from history."); // Changed to getPartsList().get(0)
        assertTrue(finalHistory.get(1).getPartsList().get(0).getText().contains(expectedResponseText), "Assistant response missing from history."); // Changed to getPartsList().get(0)

        verify(mockAdapter, times(1)).generateContent(
            eq("gemini-pro"),
            anyList(), 
            eq(sessionConfig)
        );
    }
}
