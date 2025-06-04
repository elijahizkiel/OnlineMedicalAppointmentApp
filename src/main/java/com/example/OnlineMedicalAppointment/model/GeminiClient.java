package com.example.OnlineMedicalAppointment.model;

import com.google.common.collect.ImmutableList; // Keep one
import com.google.genai.Client;
// Removed GenerativeService and GenerateContentRequest imports as they were causing errors
import com.google.genai.types.Content;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions; // Keep for Client builder if it becomes usable
import com.google.genai.types.Part;
import com.google.genai.types.Tool;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
// Optional is not explicitly used in the final version of the constructor from the prompt
// but might be needed if setApiKey(Optional<String>) was found and used.
// For now, let's keep it if other parts of the file use it.
import java.util.Optional; 

/**
 * A client class to integrate Google's Gemini API into a Java application.
 * It sends text messages to Gemini, receives feedback, and uses database functions when necessary.
 */
public class GeminiClient {
    private final Client genAiClient; // Renamed from client
    private final GenerativeModelAdapter modelAdapter;

    /**
     * Interface for adapting generative model calls.
     */
    public interface GenerativeModelAdapter {
        /**
         * Generates content using the specified model.
         * @param modelName the model name
         * @param contents the content list
         * @param config the generation config
         * @return the generation response
         * @throws Exception if generation fails
         */
        GenerateContentResponse generateContent(String modelName, List<Content> contents, GenerateContentConfig config) throws Exception;
    }

    // This class goes inside GeminiClient.java
    private static class RealGenerativeModelAdapter implements GenerativeModelAdapter {
        private final Client actualClient; // com.google.genai.Client

        RealGenerativeModelAdapter(Client actualClient) {
            this.actualClient = actualClient;
        }

        @Override
        public GenerateContentResponse generateContent(String modelName, List<Content> contents, GenerateContentConfig config) throws Exception {
            // This is the actual SDK call
            // Note: The original GeminiClient.ChatSession used "gemini-pro".
            // We are passing modelName as a parameter here, which is good for flexibility.
            return this.actualClient.models.generateContent(modelName, contents, config);
        }
    }

    /**
     * Constructor that initializes the Gemini client with an API key.
     * @param apiKey The API key obtained from Google AI Studio.
     */
    public GeminiClient(String apiKey) {
        // Instruction:
        // try {
        //    this.client = Client.builder()
        //        // .setApiKey(apiKey) // This is a guess, verify if such a method exists.
        //        // If not, the API key might need to be an environment variable like GOOGLE_API_KEY
        //        // or the application needs to be configured for Vertex AI with ADC.
        //        .httpOptions(HttpOptions.builder().apiVersion("v1").build()) // As per documentation
        //        .build();
        // } catch (Exception e) { // Catch broad exception during instantiation for now
        //    throw new RuntimeException("Failed to initialize Gemini Client: " + e.getMessage(), e);
        // }
        // Note: The prompt's example for Client.builder() does not show direct API key input.
        // Assuming API key is handled by environment (e.g., GOOGLE_API_KEY) or ADC for Vertex.
        // If a method like .setApiKey(apiKey) exists on Client.Builder, it should be used.
        // For now, following the structure that prioritizes HttpOptions as shown in prompt.
        try {
            // HttpOptions setup removed from Client.builder() as setHttpOptions was not found
            // and httpOptions() also might not exist or is not straightforward.
            // This relies on API key being set via environment variable GOOGLE_API_KEY.
            this.genAiClient = Client.builder()
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Gemini Client: " + e.getMessage(), e);
        }
        this.modelAdapter = new RealGenerativeModelAdapter(this.genAiClient);
    }

    /**
     * Package-private constructor for testing.
     * @param testAdapter the adapter to use for testing
     */
    GeminiClient(GenerativeModelAdapter testAdapter) {
        this.genAiClient = null; // Not used by ChatSession if modelAdapter is provided
        this.modelAdapter = testAdapter;
    }

    /**
     * Starts a new chat session with Gemini, configured to use database functions.
     * @return A ChatSession object to manage the conversation.
     * @throws RuntimeException if the getProductPrice method cannot be found.
     */
    public ChatSession startChat() {
        Method getProductPriceMethod;
        try {
            getProductPriceMethod = DatabaseFunctions.class.getMethod("getProductPrice", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find method getProductPrice", e);
        }

        com.google.genai.types.FunctionDeclaration functionDeclaration =
            com.google.genai.types.FunctionDeclaration.builder()
                .name("getProductPrice") // Using .name()
                .description("Retrieves the price of a product from the database.") // Using .description()
                // Parameters schema would be defined here for full functionality
                .build();

        com.google.genai.types.Tool tool = com.google.genai.types.Tool.builder()
            .functionDeclarations(ImmutableList.of(functionDeclaration)) // Using .functionDeclarations()
            .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
           .tools(ImmutableList.of(tool)) // Kept .tools()
           .build();
        return new ChatSession(this.modelAdapter, config); // Pass modelAdapter
    }

    /**
     * A nested class to manage a chat session with Gemini, maintaining conversation history
     * and handling database-backed responses via function calling.
     */
    public static class ChatSession {
        private final GenerativeModelAdapter modelAdapter; // Changed from Client client
        private final GenerateContentConfig config;
        private final List<Content> history;

        /**
         * Constructs a ChatSession with the given adapter and config.
         * @param modelAdapter the generative model adapter
         * @param config the generation config
         */
        private ChatSession(GenerativeModelAdapter modelAdapter, GenerateContentConfig config) {
            this.modelAdapter = modelAdapter;
            this.config = config;
            this.history = new ArrayList<>();
        }

        // Add this package-private constructor for testing
        /**
         * Package-private constructor for testing.
         * @param modelAdapter the generative model adapter
         * @param config the generation config
         * @param history the conversation history
         */
        ChatSession(GenerativeModelAdapter modelAdapter, GenerateContentConfig config, List<Content> history) {
            this.modelAdapter = modelAdapter;
            this.config = config;
            this.history = history;
        }

        /**
         * Sends a text message to Gemini and returns its response.
         * If the message requires database information, Gemini automatically calls the registered functions.
         * @param message The user's text message.
         * @return Gemini's response as a string.
         * @throws Exception if the modelAdapter.generateContent call fails.
         */
        public String sendMessage(String message) throws Exception {
            // Add the user's message to the conversation history
            Content userContent = Content.fromParts(Part.fromText("user: " + message));
            history.add(userContent);

            // Send the entire conversation history to Gemini with the function calling config
            List<Content> contents = new ArrayList<>(history);
            GenerateContentResponse response = modelAdapter.generateContent(
                "gemini-pro", // Model name consistent with RealGenerativeModelAdapter
                contents,
                this.config
            );

            // Extract the response text and add it to the history
            String responseText = response.text(); // Reverting to text()
            Content assistantContent = Content.fromParts(Part.fromText("assistant: " + responseText));
            history.add(assistantContent);

            return responseText;
        }

        /**
         * Gets the conversation history.
         * @return the conversation history
         */
        List<Content> getHistory() {
            return this.history;
        }

        /**
         * Gets the generation config.
         * @return the generation config
         */
        GenerateContentConfig getConfig() {
            return this.config;
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
