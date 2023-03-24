package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.openAI.OpenAIRequestDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.utils.enums.OpenAiModels;

import java.util.ArrayList;

public class OpenAIRequestDTOMapper {
    public OpenAIRequestDTO mapToDTO(OpenAiModels model,
                                     ConversationDTO conversationDTO,
                                     int maxTokens,
                                     double temperature) {
        ArrayList<OpenAIPromptDTO> openAIPromptDTOS = conversationDTO.getConversation();

        return OpenAIRequestDTO.builder()
                .model(model.toString())
                        .maxTokens(maxTokens)
                                .temperature(temperature)
                                        .conversation(openAIPromptDTOS)
                .build();
    }
}
