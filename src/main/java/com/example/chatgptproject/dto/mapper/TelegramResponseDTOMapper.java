package com.example.chatgptproject.dto.mapper;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;

public class TelegramResponseDTOMapper {
    public TelegramMessageResponseDTO mapToDTO(Long chatId, String message) {
        return TelegramMessageResponseDTO.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }
}
