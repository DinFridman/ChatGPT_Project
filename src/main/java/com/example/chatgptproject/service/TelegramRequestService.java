package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;

import java.io.IOException;

public interface TelegramRequestService {
    TelegramResponseDTO handleTelegramRequest(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException;
    OpenAIPromptDTO generateAnswerByRequestHandler(ConversationDTO conversationDTO)
            throws IOException, InterruptedException;
    void addOpenAIMessageToConversation(ChatMessageDTO chatMessageDTO,
                                        OpenAIPromptDTO openAIPromptDTO);
    TelegramResponseDTO createTelegramResponse(Long chatId, String content);
    boolean isShareConversationRequest(ChatMessageDTO chatMessageDTO);
    ChatMessageDTO createChatMessageDTO(ChatMessageDTO chatMessageDTO,
                                        String role, String content);
    ConversationDTO getCurrentConversation(Long chatId);
    TelegramResponseDTO handleShareConversationRequestAndReturnResponse(
            ConversationDTO conversationDTO, ChatMessageDTO chatMessageDTO);
    void addUserMessageToConversation(ChatMessageDTO chatMessageDTO);


}
