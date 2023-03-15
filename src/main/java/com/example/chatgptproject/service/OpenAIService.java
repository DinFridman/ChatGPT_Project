package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.http.HttpEntity;

public interface OpenAIService {
    OpenAIPromptDTO generateAnswer(ConversationDTO conversationDTO)
            throws JsonProcessingException, JSONException;
}
