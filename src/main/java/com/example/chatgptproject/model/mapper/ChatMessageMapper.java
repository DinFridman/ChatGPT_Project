package com.example.chatgptproject.model.mapper;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.model.ChatMessageEntity;
import org.jetbrains.annotations.NotNull;


public class ChatMessageMapper {
    public ChatMessageEntity mapToEntity(@NotNull ChatMessageDTO chatMessageDTO, AppUser user) {
        return new ChatMessageEntity(chatMessageDTO.getUpdateId(),
                chatMessageDTO.getChatId(),
                chatMessageDTO.getMessage(),
                chatMessageDTO.getRole(),
                user);
    }
}
