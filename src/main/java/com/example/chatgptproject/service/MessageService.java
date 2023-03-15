package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;

import java.util.ArrayList;

public interface MessageService {

    void addChatMessage(ChatMessageDTO chatMessageDTO);
    ConversationDTO addChatMessageAndGetConversation(ChatMessageDTO chatMessageDTO);
    ArrayList<OpenAIPromptDTO> getConversationById(Long chatId);

}
