package com.chatgptproject.service;

import com.chatgptproject.dto.ChatMessageDTO;

public interface ConversationSenderService {
    String handleSendConversationRequest(ChatMessageDTO chatMessageDTO);
}
