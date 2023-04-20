package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;

import java.io.IOException;

public interface TelegramRequestService {
    TelegramMessageResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException;
    OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException;
    ChatMessageDTO createChatMessageDTOForOpenAIResponse(ChatMessageDTO chatMessageDTO,
                                               OpenAIPromptDTO openAIPromptDTO);
    TelegramMessageResponseDTO createTelegramResponse(Long chatId, String content);
    boolean isShareConversationRequest(ChatMessageDTO chatMessageDTO);
    ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                        String role, String content);
    ConversationDTO getCurrentConversation(ChatMessageDTO chatMessageDTO);
    void handleShareConversationRequest(
            ChatMessageDTO chatMessageDTO, String recipient);
    void addChatMessageToConversation(ChatMessageDTO chatMessageDTO);


}
