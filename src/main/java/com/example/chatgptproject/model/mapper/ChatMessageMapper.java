package com.example.chatgptproject.model.mapper;

import com.example.chatgptproject.dto.TelegramRequestDTO;
import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.utils.Constants;
import org.jetbrains.annotations.NotNull;


public class ChatMessageMapper {
    public ChatMessageEntity mapToEntity(@NotNull TelegramRequestDTO telegramModelDTO) {
        return ChatMessageEntity.builder()
                .message(telegramModelDTO.getMessage())
                        .updateId(telegramModelDTO.getUpdateId())
                                .chatId(telegramModelDTO.getChatId())
                                        .userId(telegramModelDTO.getUserId())
                                                .role(Constants.USER_ROLE)
                .build();
    }
}
