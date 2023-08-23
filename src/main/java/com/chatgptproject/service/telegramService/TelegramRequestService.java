package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.dto.TelegramMessageResponseDTO;
import java.io.IOException;

public interface TelegramRequestService {
    TelegramMessageResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException;
    void handleShareConversationRequest(
            ChatMessageDTO chatMessageDTO, String recipient);


}
