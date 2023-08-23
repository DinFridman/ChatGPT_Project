package com.chatgptproject.service;

import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.model.AppUserEntity;

public interface MessagesService {
    void addChatMessage(ChatMessageDTO chatMessageDTO);
    AppUserEntity getAppUserFromChatMessageDTO(ChatMessageDTO chatMessageDTO);
    ConversationDTO getConversationByUserId(Long chatId);
}
