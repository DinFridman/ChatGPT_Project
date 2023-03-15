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
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OpenAIServiceImpl implements OpenAIService{
    private final RestTemplate restTemplate = new RestTemplate();//TODO: DEPENDENCY INJECTION
    private final ObjectMapper objectMapper = new ObjectMapper();//TODO: Dependency Injection
    private final OpenAIPromptDTOMapper openAIPromptDTOMapper;
    private final ChatCompletionDTOMapper chatCompletionDTOMapper;
    private HttpEntity<String> requestEntity;
    private static final Logger logger = LogManager.getLogger("generateAnswerService-logger");


    @Override
    public OpenAIPromptDTO generateAnswer(ConversationDTO conversationDTO)
            throws JsonProcessingException, JSONException {

        requestEntity = createRequestEntity(conversationDTO);

        logger.info("----------AI_Request : " + requestEntity + "----------");

        String response = restTemplate.postForObject(Constants.CHAT_COMPLETION_URL, requestEntity, String.class);
        response.trim();
        logger.info("----------AI_Response has sent successfully----------");
        logger.debug("----------AI_Response : " + response + "----------");

        String message = getDataFromResponse(response, "content");
        String role = getDataFromResponse(response,"role");

        return openAIPromptDTOMapper.mapToOpenAIPromptDTO(role,message);
    }


    private String getDataFromResponse(String json, String dataRequested)
            throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        String data = objectMapper.treeToValue(
                root.path("choices")
                        .path(0).path("message")
                        .path(dataRequested), String.class);
        return data;
    }


    private HttpEntity<String> createRequestEntity(
            ConversationDTO conversationDTO) throws JSONException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", Constants.AUTHORIZATION_KEY);

        ChatCompletionDTO chatCompletionDTO = chatCompletionDTOMapper
                .mapToDTO(OpenAiModels.CHAT_COMPLETION, conversationDTO);

        String body = objectMapper.writeValueAsString(chatCompletionDTO);

        return new HttpEntity<String>(body, headers);
    }
}
