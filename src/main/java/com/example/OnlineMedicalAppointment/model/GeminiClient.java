package com.example.OnlineMedicalAppointment.model;

import com.google.common.collect.ImmutableList;
// import com.google.genai.Client; // Removed unused import
import com.google.generativeai.GenerativeModel; // Corrected import
import com.google.generativeai.Content; // Corrected import
import com.google.generativeai.GenerateContentConfig; // Corrected import
import com.google.generativeai.GenerateContentResponse; // Corrected import
import com.google.generativeai.Part; // Corrected import
import com.google.generativeai.Tool; // Corrected import
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;
/**
 * A client class to integrate Google's Gemini API into a Java application.
 */
public class GeminiClient {
    private final GenerativeModel model; // Changed type to GenerativeModel

    /**
     * Constructor that initializes the Gemini client with an API key.
     * @param apiKey The API key obtained from Google AI Studio.
     */
    public GeminiClient(String apiKey) {
        // Use GenerativeModel.builder and specify a model name
        this.model = GenerativeModel.builder()
            .setModelName("gemini-1.5-flash-latest") // Specify model name
            .setApiKey(apiKey)
            .build();
    }
    

    /**
     * Starts a new chat session with Gemini, configured to use database functions.
     * @return A ChatSession object to manage the conversation.
     * @throws NoSuchMethodException If the database function method cannot be found.
     */
    public ChatSession startChat() throws NoSuchMethodException {
        // Register the database function for automatic function calling
        Method getAppointments = DatabaseAccessor.class.getMethod("getAppointments", String.class);
        // Wrap the Method in a Tool object
        Tool getAppointmentsTool = Tool.fromMethod(getAppointments);
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(ImmutableList.of(getAppointmentsTool)) // Use the Tool object
            .build();
        return new ChatSession(model, config); // Pass the GenerativeModel
    }

    /**
     * A nested class to manage a chat session with Gemini, maintaining conversation history
     * and handling database-backed responses via function calling.
     */
    public static class ChatSession {
        private final GenerativeModel model; // Changed type to GenerativeModel
        private final GenerateContentConfig config;
        private final List<Content> history;

        private ChatSession(GenerativeModel model, GenerateContentConfig config) { // Changed parameter type
            this.model = model;
            this.config = config;
            this.history = new ArrayList<>();
        }

        /**
         * Sends a text message to Gemini and returns its response.
         * If the message requires database information, Gemini automatically calls the registered functions.
         * @param message The user's text message.
         * @return Gemini's response as a string.
         */
        public String sendMessage(String message) {
            // Add the user's message to the conversation history
            Content userContent = Content.fromParts(Part.fromText("user: " + message));
            history.add(userContent);

            // Send the entire conversation history to Gemini with the function calling config
            List<Content> contents = new ArrayList<>(history);
            // Use the GenerativeModel instance to generate content
            GenerateContentResponse response = model.generateContent(contents, config);

            // Extract the response text and add it to the history
            String responseText = response.text();
            Content assistantContent = Content.fromParts(Part.fromText("assistant: " + responseText));
            history.add(assistantContent);

            return responseText;
        }
    }
}
