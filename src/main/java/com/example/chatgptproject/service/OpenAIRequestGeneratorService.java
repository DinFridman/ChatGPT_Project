package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIRequestDTO;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface OpenAIRequestGeneratorService {
    String createOpenAICompletion(ConversationDTO conversationDTO)
            throws IOException, InterruptedException;
    String sendChatgptRequest(String body)
            throws IOException, InterruptedException;
    HttpRequest createHttpRequest(String body);
    OpenAIRequestDTO createOpenAIRequestDTO(ConversationDTO conversationDTO);
}
