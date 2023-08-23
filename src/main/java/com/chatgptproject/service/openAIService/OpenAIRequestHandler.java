package com.chatgptproject.service.openAIService;

import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface OpenAIRequestHandler {
    OpenAIPromptDTO generateAnswer(ConversationDTO conversationDTO)
            throws IOException, InterruptedException;
    OpenAIPromptDTO deserializePromptFromResponse(String responseBody)
            throws JsonProcessingException;
}
