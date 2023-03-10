package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.ChatAnswerDTO;

public class ChatAnswerDTOMapper {
    public ChatAnswerDTO mapToChatAnswerDTO(Long chatId, String message) {
        return ChatAnswerDTO.builder()
                .chatId(chatId)
                .message(message)
                .build();
    }
}
