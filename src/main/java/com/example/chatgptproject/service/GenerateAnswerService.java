package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatCompletionDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.OpenAIPromptDTO;
import com.example.chatgptproject.dto.mapper.ChatCompletionDTOMapper;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.utils.Constants;
import com.example.chatgptproject.utils.enums.OpenAiModels;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GenerateAnswerService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;
    private final ChatCompletionDTOMapper chatCompletionDTOMapper;
    private HttpEntity<String> request;
    private static final Logger logger = LogManager.getLogger("generateAnswerService-logger");

    public OpenAIPromptDTO generateAnswer
            (ConversationDTO conversationDTO) throws JsonProcessingException {

        request = createRequest(conversationDTO);

        logger.info("----------AI_Request : " + request + "----------");

        String response = restTemplate.postForObject(Constants.CHAT_COMPLETION_URL, request, String.class);
        response.trim();
        logger.info("----------AI_Response has sent successfully----------");
        logger.debug("----------AI_Response : " + response + "----------");

        String message = getDataFromResponse(response, "content");
        String role = getDataFromResponse(response,"role");

        return openAIPromptDTOMapper.mapToOpenAIPromptDTO(role,message);
    }


    private String getDataFromResponse(String json, String dataRequested) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        String data = objectMapper.treeToValue(
                root.path("choices")
                        .path(0).path("message")
                        .path(dataRequested), String.class);
        return data;
    }

    private HttpEntity<String> createRequest(
            ConversationDTO conversationDTO) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", Constants.AUTHORIZATION_KEY);

        ChatCompletionDTO chatCompletionDTO = chatCompletionDTOMapper
                .mapToDTO(OpenAiModels.CHAT_COMPLETION, conversationDTO);

        String body = objectMapper.writeValueAsString(chatCompletionDTO);

        return new HttpEntity<>(body, headers);
    }
}
