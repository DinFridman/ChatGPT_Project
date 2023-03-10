package com.example.chatgptproject.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAIPromptDTO {
    private final String role;
    private final String content;

    @Override
    public String toString() {
        return "{" +
                "role:" + role +
                ", content: " + content +
                '}';
    }
}
