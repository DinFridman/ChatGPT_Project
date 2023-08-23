package com.chatgptproject.service;

import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.EmailDetailsDTO;

public interface EmailService {
    void handleShareConversationRequest(ConversationDTO conversationDTO, String messageBody);
    void sendMailWithAttachment(EmailDetailsDTO details);
}
