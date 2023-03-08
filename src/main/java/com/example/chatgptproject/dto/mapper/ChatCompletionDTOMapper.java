package com.example.chatgptproject.dto.mapper;


import com.example.chatgptproject.dto.ChatCompletionDTO;
import com.example.chatgptproject.dto.OpenAIMessageDTO;
import com.example.chatgptproject.dto.TelegramRequestDTO;
import com.example.chatgptproject.utils.enums.OpenAiModels;
import com.example.chatgptproject.utils.enums.Roles;

import java.util.ArrayList;

public class ChatCompletionDTOMapper {
    public ChatCompletionDTO mapToDTO(TelegramRequestDTO telegramRequestDTO, OpenAiModels model) {
        ArrayList<OpenAIMessageDTO> messageModels = new ArrayList<>();
        messageModels.add( OpenAIMessageDTO.builder()
                .content(telegramRequestDTO.getMessage())
                .role(Roles.USER.toString()).build() ); //TODO: User is always defined

        return ChatCompletionDTO.builder()
                .model(model.toString())
                        .maxTokens(2000)
                                .temperature(0.8)
                                        .conversation(messageModels)
                .build();
    }
}
