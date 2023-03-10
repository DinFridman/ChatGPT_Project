package com.example.chatgptproject.model.mapper;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.utils.enums.Roles;
import org.jetbrains.annotations.NotNull;


public class ChatMessageMapper {
    public ChatMessageEntity mapToEntity(@NotNull ChatMessageDTO chatMessageDTO) {
        return new ChatMessageEntity(chatMessageDTO.getUpdateId(),
                chatMessageDTO.getChatId(),
                chatMessageDTO.getMessage(),
                chatMessageDTO.getUserId(),
                chatMessageDTO.getRole());
    }
}
