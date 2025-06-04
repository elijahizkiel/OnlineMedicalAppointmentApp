package com.example.OnlineMedicalAppointment.model;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Tool;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

public class GeminiClient {
    private final Client client = Client.builder()
            .apiKey("AIzaSyCz4P3Ywm2e_7AG8Z67Phr7GVMUaW7D8-w")
            .build();

    private final GenerateContentConfig config;
    private final List<String> chatHistory = new ArrayList<>();

    public GeminiClient() {
        try {
            Class<?> dbClass = DatabaseAccessor.class;
            ImmutableList<Method> functions = ImmutableList.of(
                dbClass.getMethod("getDoctorsBySpecialty", String.class),
                dbClass.getMethod("getDoctors"),
                dbClass.getMethod("getMessages", String.class),
                dbClass.getMethod("getAppointments", int.class),
                dbClass.getMethod("getChatRoomID", int.class),
                dbClass.getMethod("addUser", User.class),
                dbClass.getMethod("getByUsername", String.class),
                dbClass.getMethod("getUserByID", int.class)
            );
            this.config = GenerateContentConfig.builder()
                .tools(
                    ImmutableList.of(
                        Tool.builder().functions(functions).build()
                    )
                )
                .build();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to register DatabaseAccessor functions", e);
        }
    }

    // Stores chat history, manages context, and uses autofunctioncalling
    public String generateContent(String prompt) {
        chatHistory.add("User: " + prompt);
        GenerateContentResponse response = client.models.generateContent(
            "gemini-2.0-flash-001",
            prompt,
            config
        );
        String reply = response.text();
        chatHistory.add("Gemini: " + reply);

        StringBuilder sb = new StringBuilder();
        sb.append(reply);

        // Optionally append function calling history if present
        Optional<?> history = response.automaticFunctionCallingHistory();
        history.ifPresent(h -> sb.append("\nFunction calling history: ").append(h));

        return sb.toString();
    }

    public List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }
}