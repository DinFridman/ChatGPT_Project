package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import java.io.IOException;

public interface TelegramRequestService {
    TelegramMessageResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException;
    void handleShareConversationRequest(
            ChatMessageDTO chatMessageDTO, String recipient);


}
