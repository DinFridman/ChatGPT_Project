package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.ChatCompletionDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.example.chatgptproject.utils.enums.OpenAiModels;

import java.util.ArrayList;

public class ChatCompletionDTOMapper {
    public ChatCompletionDTO mapToDTO( OpenAiModels model,ConversationDTO conversationDTO) {
        ArrayList<OpenAIPromptDTO> openAIPromptDTOS = conversationDTO.getConversation();

        return ChatCompletionDTO.builder()
                .model(model.toString())
                        .maxTokens(2000)
                                .temperature(0.8)
                                        .conversation(openAIPromptDTOS)
                .build();
    }
}
