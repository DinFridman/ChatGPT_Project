package com.chatgptproject.dto.mapper;

import com.chatgptproject.dto.TelegramMessageResponseDTO;

public class TelegramResponseDTOMapper {
    public TelegramMessageResponseDTO mapToDTO(Long chatId, String message) {
        return TelegramMessageResponseDTO.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }
}
