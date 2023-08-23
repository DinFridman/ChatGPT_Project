package com.chatgptproject.dto.openAI;


public record OpenAIPromptDTO(String role, String content) {
    @Override
    public String toString() {
        return "{" +
                "role:" + role +
                ", content: " + content +
                '}';
    }
}
