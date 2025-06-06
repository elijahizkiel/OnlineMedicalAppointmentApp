package com.example.OnlineMedicalAppointment.model;


import com.google.genai.Client;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

import java.util.List;

import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

/**
 * GeminiClient is a client for interacting with the Gemini AI model.
 * It handles generating content based on user queries and chat history.
 */
public class GeminiClient {
    private final String MODEL_NAME = "gemini-2.0-flash";
    private final String SYSTEM_PROMPT = "You are a helpful assistant for an online medical appointment system." + 
        " You are given a user's query and you need to respond to it in a way that is helpful and informative." +
        " You are also given a chat history of the conversation between the user and the assistant." + 
        " You need to use the chat history to help you respond to the user's query." + 
        "You are also given a list of available doctors and their specialties.";
        
    private static final String API_KEY = "AIzaSyCz4P3Ywm2e_7AG8Z67Phr7GVMUaW7D8-w";
    private StringBuilder chatHistory = new StringBuilder();
    Client client;
    public GeminiClient() {
        client = Client.builder().apiKey(API_KEY).build();

    }
    
    // Helper to add a message to chat history
    private void addToChatHistory(String role, String content) {
        chatHistory.append(role).append(": ").append(content).append("\n");
    }

    public String generateContent(String prompt) {
        String reply;
        try {
            // Build config (default)
            GenerateContentConfig config;
            
            // Function calling logic
            if (prompt.contains("time")
            || prompt.contains("date")
            || prompt.contains("book") 
            || prompt.contains("schedule") 
            || prompt.contains("appointment")) {
                List<Appointment> appointments = DatabaseAccessor.getAllAppointments();
                StringBuilder appointmentsInString = new StringBuilder();
                appointments.forEach(appointment -> {
                    appointmentsInString.append(appointment.toString()).append("\n");
                });
                config = GenerateContentConfig.builder().systemInstruction(Content.
                fromParts(Part.fromText(SYSTEM_PROMPT), Part.fromText("Use the following data as primary source to answer the question of the user: " + appointmentsInString.toString()))).build();
                System.out.println(appointmentsInString.toString());
            }else {
                config =  GenerateContentConfig
                .builder().systemInstruction(Content.
                fromParts(Part.fromText(SYSTEM_PROMPT), Part.fromText("make answers brief unless required by the user"))).build();
            }
            addToChatHistory("user", prompt);
            // Generate content
            GenerateContentResponse response = client.models.generateContent(MODEL_NAME, chatHistory.toString(), config);

            reply = response.text();
            addToChatHistory("model", reply);
        } catch (Exception e) {
            throw new RuntimeException("Error generating content: " + e.getMessage(), e);
        }
        return reply;
    }
    
    public void clearChatHistory() {
        chatHistory = new StringBuilder();
    }
}