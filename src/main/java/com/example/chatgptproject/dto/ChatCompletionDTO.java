package com.example.chatgptproject.dto;

import lombok.Builder;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Data
@Builder
public class ChatCompletionDTO {
    private String model;
    private int maxTokens;
    private double temperature;
    private ArrayList<OpenAIMessageDTO> conversation;

    public JSONObject getModelAsJSON() throws JSONException {
        return new JSONObject()
                .put("model",model)
                .put("messages", conversation)
                .put("max_tokens", 2000)
                .put("temperature", 0.5);
    }
}
