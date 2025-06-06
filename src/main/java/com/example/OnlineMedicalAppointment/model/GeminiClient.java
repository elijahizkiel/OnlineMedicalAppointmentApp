package com.example.OnlineMedicalAppointment.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeminiClient {
    private static final String API_KEY = "AIzaSyCz4P3Ywm2e_7AG8Z67Phr7GVMUaW7D8-w";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + API_KEY;
    
    private final List<Content> chatHistory = new ArrayList<>();
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    // Inner class for request/response structure
    static class Content {
        String role;
        List<Part> parts;
        
        Content(String role, String text) {
            this.role = role;
            this.parts = List.of(new Part(text));
        }
    }
    
    static class Part {
        String text;
        
        Part(String text) {
            this.text = text;
        }
    }
    
    static class RequestBody {
        
        RequestBody(List<Content> contents) {
        }
    }
    
    static class ResponseBody {
        List<Candidate> candidates;
    }
    
    static class Candidate {
        Content content;
    }

    public GeminiClient() {
        // Initialize with a system message
        chatHistory.add(new Content("system", "You are a helpful assistant for an online medical appointment system."));
    }

    public String generateContent(String prompt) {
        try {
            // Add user message to chat history
            chatHistory.add(new Content("user", prompt));
            
            // Prepare request payload
            RequestBody requestBody = new RequestBody(chatHistory);
            String json = gson.toJson(requestBody);
            
            // Create HTTP request
            Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(okhttp3.RequestBody.create(json, MediaType.get("application/json")))
                .build();
            
            // Execute request
            Response response = httpClient.newCall(request).execute();
            
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            
            // Parse response
            ResponseBody responseBody = gson.fromJson(response.body().charStream(), ResponseBody.class);
            if (responseBody.candidates == null || responseBody.candidates.isEmpty()) {
                throw new IOException("No candidates in response");
            }
            
            String reply = responseBody.candidates.get(0).content.parts.get(0).text;
            
            // Add model's response to chat history
            chatHistory.add(new Content("assistant", reply));
            
            return reply;
        } catch (Exception e) {
            throw new RuntimeException("Error generating content: " + e.getMessage(), e);
        }
    }

    public List<Content> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }
    
    public void clearChatHistory() {
        chatHistory.clear();
    }
}