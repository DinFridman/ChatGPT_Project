package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OpenAIResponseService {
    OpenAIPromptDTO getPromptFromResponse(String responseBody)
            throws JsonProcessingException;
}
