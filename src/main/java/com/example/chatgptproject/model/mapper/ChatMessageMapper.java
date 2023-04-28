package com.example.chatgptproject.model.mapper;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.model.ChatMessageEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
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
