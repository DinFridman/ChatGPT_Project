package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

public interface RequestService {
    ChatAnswerDTO generateAnswer(ChatMessageDTO chatMessageDTO)
            throws JsonProcessingException, JSONException;
}
