package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.dto.openAI.OpenAIResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OpenAIRequestHandler {
    private final OpenAIRequestGeneratorService openAIRequestGeneratorService;
    private final ObjectMapper objectMapper;

    public OpenAIPromptDTO generateAnswer(ConversationDTO conversationDTO) throws Exception {
        String responseBody = openAIRequestGeneratorService.createOpenAICompletion(conversationDTO);
        return getPromptFromResponse(responseBody);

    }


    public OpenAIPromptDTO getPromptFromResponse(String responseBody)
            throws JsonProcessingException {
        OpenAIResponseDTO response = objectMapper.readValue(responseBody, OpenAIResponseDTO.class);
        OpenAIPromptDTO openAIPromptDTO = response.getChoices().get(0).getMessage();
        log.info("----------AI_Response has sent successfully----------");
        log.debug("----------AI_Response : " + response + "----------");
        return openAIPromptDTO;
    }




}
