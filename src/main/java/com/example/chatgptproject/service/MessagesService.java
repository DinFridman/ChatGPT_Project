package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.model.AppUserEntity;

public interface MessagesService {
    void addChatMessage(ChatMessageDTO chatMessageDTO);
    AppUserEntity getAppUserFromChatMessageDTO(ChatMessageDTO chatMessageDTO);
    ConversationDTO getConversationById(Long chatId);
}
