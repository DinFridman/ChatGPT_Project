package com.example.chatgptproject.dto.openAI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;

@Data
@Builder
public class OpenAIRequestDTO {
    private String model;
    @JsonProperty("max_tokens")
    private int maxTokens;
    private double temperature;
    @JsonProperty("messages")
    private ArrayList<OpenAIPromptDTO> conversation;

}
