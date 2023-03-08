package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.OpenAIMessageDTO;

public class ChatMessageDTOMapper {
    public OpenAIMessageDTO mapToChatMassageDTO(String role, String message) {
        return OpenAIMessageDTO.builder()
                    .role(role)
                    .content(message)
                    .build();
    }
}
