package com.example.chatgptproject.dto.openAI;


import lombok.Getter;


public record OpenAIPromptDTO(String role, String content) {
    @Override
    public String toString() {
        return "{" +
                "role:" + role +
                ", content: " + content +
                '}';
    }
}
