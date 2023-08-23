package com.chatgptproject.dto.mapper;


import com.chatgptproject.utils.enums.OpenAiModels;
import com.chatgptproject.dto.openAI.OpenAIRequestDTO;
import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.openAI.OpenAIPromptDTO;

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
