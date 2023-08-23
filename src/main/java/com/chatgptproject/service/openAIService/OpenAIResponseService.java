package com.chatgptproject.service.openAIService;

import com.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OpenAIResponseService {
    OpenAIPromptDTO getPromptFromResponse(String responseBody)
            throws JsonProcessingException;
}
