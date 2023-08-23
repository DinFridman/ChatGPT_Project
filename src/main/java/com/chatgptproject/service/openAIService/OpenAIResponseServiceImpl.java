package com.chatgptproject.service.openAIService;

import com.chatgptproject.dto.openAI.OpenAIResponseDTO;
import com.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OpenAIResponseServiceImpl implements OpenAIResponseService {
    private final ObjectMapper objectMapper;

    @Override
    public OpenAIPromptDTO getPromptFromResponse(String responseBody)
            throws JsonProcessingException {
        OpenAIResponseDTO response = objectMapper.readValue(responseBody, OpenAIResponseDTO.class);
        OpenAIPromptDTO openAIPromptDTO = response.getChoices().get(0).getMessage();

        log.info("----------AI_Response has sent successfully----------");
        log.debug("----------AI_Response : " + response + "----------");

        return openAIPromptDTO;
    }
}
