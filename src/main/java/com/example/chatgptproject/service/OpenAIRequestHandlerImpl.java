package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
@RequiredArgsConstructor
public class OpenAIRequestHandlerImpl implements OpenAIRequestHandler{
    private final OpenAIRequestGeneratorServiceImpl openAIRequestGeneratorServiceImpl;
    private final OpenAIResponseServiceImpl openAIResponseServiceImpl;

    @Override
    public OpenAIPromptDTO generateAnswer(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        String responseBody =
                openAIRequestGeneratorServiceImpl.createOpenAICompletion(conversationDTO);

        log.debug("Response body : " + responseBody);

        return deserializePromptFromResponse(responseBody);

    }

    public OpenAIPromptDTO deserializePromptFromResponse(String responseBody)
            throws JsonProcessingException {
        return openAIResponseServiceImpl.getPromptFromResponse(responseBody);
    }

}
