package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.EmailDetailsDTO;

public interface EmailService {
    void handleShareConversationRequest(ConversationDTO conversationDTO, String messageBody);
    void sendMailWithAttachment(EmailDetailsDTO details);
}
