package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;

public class OpenAIPromptDTOMapper {
    public OpenAIPromptDTO mapToOpenAIPromptDTO(String role, String message) {
        return new OpenAIPromptDTO(role,message);
    }
}
