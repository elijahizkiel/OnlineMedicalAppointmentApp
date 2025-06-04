package com.example.OnlineMedicalAppointment.model;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.GenerativeModel; // Explicitly import this
import com.google.genai.types.*; // Wildcard for Content, Part, GenerateContentResponse, GenerateContentConfig, Candidate

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeminiClientTest {

    @Mock
    private Client mockSdkClient; // Mock the com.google.genai.Client

    @Mock
    private GenerativeModel mockGenerativeModel; // Mock com.google.genai.GenerativeModel

    private GeminiClient geminiClientInstance; // The class containing ChatSession
    private GenerateContentConfig testConfig; // To hold config

    @BeforeEach
    void setUp() throws Exception {
        // This setup is for the GeminiClient instance that *contains* ChatSession
        // It's used to get a valid config from startChat() and to instantiate ChatSession via its inner class constructor.
        // GOOGLE_API_KEY needs to be set for this part if GeminiClient constructor needs it.
        geminiClientInstance = new GeminiClient("TEST_API_KEY_FOR_SETUP");

        // Initialize mocks for mockSdkClient and mockGenerativeModel
        // MockitoAnnotations.openMocks(this); // Handled by @ExtendWith(MockitoExtension.class)

        // Crucial: Link mockSdkClient.models to return mockGenerativeModel
        // The field 'models' in com.google.genai.Client is public and final.
        // Mocking final fields usually requires PowerMock or specific Mockito configurations.
        // Let's try to use 'lenient()' settings with Mockito for this, or if it fails,
        // the worker will need to report this specific issue.
        // A direct assignment like "mockSdkClient.models = mockGenerativeModel;" won't work on a mock.
        // Instead, we mock the behavior of accessing the 'models' field.
        lenient().when(mockSdkClient.models).thenReturn(mockGenerativeModel);


        // Extract a valid GenerateContentConfig from a real ChatSession started by a real client
        // This ensures the config object itself is valid.
        GeminiClient.ChatSession tempSessionForConfig = geminiClientInstance.startChat();
        // Using the getter instead of reflection
        testConfig = tempSessionForConfig.getConfig();
    }

    @Test
    void testStartChat_succeeds() {
        // GOOGLE_API_KEY must be set in the test environment for this to pass
        GeminiClient client = new GeminiClient("TEST_API_KEY");
        assertDoesNotThrow(() -> {
            GeminiClient.ChatSession session = client.startChat();
            assertNotNull(session);
        });
    }

    @Test
    void testSendMessage_succeeds() throws Exception {
        List<Content> history = new ArrayList<>();
        // Use the package-private constructor to inject the mocked Client and actual config
        GeminiClient.ChatSession chatSession = geminiClientInstance.new ChatSession(mockSdkClient, testConfig, history);

        String expectedResponseText = "Mocked response from Gemini for sendMessage test.";
        Part responsePart = Part.newBuilder().setText(expectedResponseText).build();
        Content responseContent = Content.newBuilder().addParts(responsePart).setRole("model").build(); // Added role
        com.google.genai.types.Candidate candidate = com.google.genai.types.Candidate.newBuilder().setContent(responseContent).build();
        GenerateContentResponse mockApiResponse = GenerateContentResponse.newBuilder().addCandidates(candidate).build();

        // Configure the mockGenerativeModel (which is returned by mockSdkClient.models)
        when(mockGenerativeModel.generateContent(
            eq("gemini-pro"),   // model name
            anyList(),          // List<Content>
            eq(testConfig)      // GenerateContentConfig
        )).thenReturn(mockApiResponse);

        String userMessage = "Hello from test";
        String actualResponse = chatSession.sendMessage(userMessage);

        assertEquals(expectedResponseText, actualResponse);
        List<Content> finalHistory = chatSession.getHistory(); // Assumes getHistory() is added
        assertEquals(2, finalHistory.size());
        assertTrue(finalHistory.get(0).getParts(0).getText().contains(userMessage)); // getParts(0) as Content has List<Part>
        assertTrue(finalHistory.get(1).getParts(0).getText().contains(expectedResponseText));  // getParts(0)

        verify(mockGenerativeModel, times(1)).generateContent(
            eq("gemini-pro"),
            anyList(),
            eq(testConfig)
        );
    }
}
