package com.example.chatgptproject.dto.mapper;

import com.example.chatgptproject.dto.TelegramResponseDTO;

public class TelegramResponseDTOMapper {
    public TelegramResponseDTO mapToDTO(Long chatId, String message) {
        return TelegramResponseDTO.builder()
                .chatId(chatId)
                .message(message)
                .method("sendMessage")
                .build();
    }
}
