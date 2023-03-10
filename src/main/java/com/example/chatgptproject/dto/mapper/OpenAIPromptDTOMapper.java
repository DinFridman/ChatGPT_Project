package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.OpenAIPromptDTO;

public class OpenAIPromptDTOMapper {
    public OpenAIPromptDTO mapToOpenAIPromptDTO(String role, String message) {
        return OpenAIPromptDTO.builder()
                    .role(role)
                    .content(message)
                    .build();
    }
}
