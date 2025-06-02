package com.example.OnlineMedicalAppointment.model;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A client class to integrate Google's Gemini API into a Java application.
 * It sends text messages to Gemini, receives feedback, and uses database functions when necessary.
 */
public class GeminiClient {
    private final Client client;

    /**
     * Constructor that initializes the Gemini client with an API key.
     * @param apiKey The API key obtained from Google AI Studio.
     */
    public GeminiClient(String apiKey) {
        this.client = new Client(apiKey);
    }

    /**
     * Starts a new chat session with Gemini, configured to use database functions.
     * @return A ChatSession object to manage the conversation.
     * @throws NoSuchMethodException If the database function method cannot be found.
     */
    public ChatSession startChat() throws NoSuchMethodException {
        // Register the database function for automatic function calling
        Method getSchedules = DatabaseAccesor.class.getMethod("getSchedules", String.class);
        GenerateContentConfig config = GenerateContentConfig.builder()
            .tools(ImmutableList.of(getProductPriceMethod))
            .build();
        return new ChatSession(client, config);
    }

    /**
     * A nested class to manage a chat session with Gemini, maintaining conversation history
     * and handling database-backed responses via function calling.
     */
    public static class ChatSession {
        private final Client client;
        private final GenerateContentConfig config;
        private final List<Content> history;

        private ChatSession(Client client, GenerateContentConfig config) {
            this.client = client;
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
            GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash", contents, config);

            // Extract the response text and add it to the history
            String responseText = response.text();
            Content assistantContent = Content.fromParts(Part.fromText("assistant: " + responseText));
            history.add(assistantContent);

            return responseText;
        }
    }
}

/**
 * A utility class containing static methods to query the database.
 * These methods are registered with Gemini for automatic function calling.
 */
class DatabaseFunctions {
    /**
     * Retrieves the price of a product from the database.
     * @param productName The name of the product.
     * @return A string containing the product's price or an error message if not found.
     */
    public static String getProductPrice(String productName) {
        // Simulate a database query (replace with actual database logic)
        if ("laptop".equalsIgnoreCase(productName)) {
            return "The price of the laptop is $1000.";
        } else if ("smartphone".equalsIgnoreCase(productName)) {
            return "The price of the smartphone is $500.";
        } else {
            return "Product not found.";
        }
    }
}
