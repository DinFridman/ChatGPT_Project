package com.chatgptproject.model.mapper;

import com.chatgptproject.model.ChatMessageEntity;
import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.model.AppUserEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


public class ChatMessageMapper {
    public ChatMessageEntity mapToEntity(@NotNull ChatMessageDTO chatMessageDTO, AppUserEntity user) {
        return new ChatMessageEntity(chatMessageDTO.getUpdateId(),
                chatMessageDTO.getChatId(),
                chatMessageDTO.getMessage(),
                chatMessageDTO.getRole(),
                user,
                LocalDateTime.now());
    }
}
