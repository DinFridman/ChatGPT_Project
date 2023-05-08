package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;

public interface ConversationSenderService {
    String handleSendConversationRequest(ChatMessageDTO chatMessageDTO);
}
