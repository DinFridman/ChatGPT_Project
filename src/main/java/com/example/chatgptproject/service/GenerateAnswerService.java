package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.OpenAIMessageDTO;
import com.example.chatgptproject.dto.TelegramRequestDTO;
import com.example.chatgptproject.dto.mapper.ChatCompletionDTOMapper;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.utils.Constants;
import com.example.chatgptproject.utils.enums.OpenAiModels;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GenerateAnswerService {
    private final RestTemplate restTemplate = new RestTemplate();//TODO: DEPENDENCY INJECTION
    private final ObjectMapper objectMapper = new ObjectMapper();//TODO: Dependency Injection
    private JSONObject requestBody;
    private final ChatMessageDTOMapper chatMessageDTOMapper = new ChatMessageDTOMapper();//TODO: Dependency Injection
    private final ChatCompletionDTOMapper chatCompletionDTOMapper = new ChatCompletionDTOMapper();//TODO: Dependency Injection
    private HttpEntity<String> requestEntity;
    private static final Logger logger = LogManager.getLogger("GenerateAnswerService-logger");



    public OpenAIMessageDTO generateAnswer(TelegramRequestDTO telegramRequestDTO) throws JsonProcessingException, JSONException {

        requestEntity = createRequestEntity(telegramRequestDTO);

        logger.info("----------AI_Request : " + requestEntity + "----------");

        String response = restTemplate.postForObject(Constants.CHAT_COMPLETION_URL, requestEntity, String.class);

        logger.info("----------AI_Response : " + response + "----------");

        String message = getDataFromResponse(response, "content");
        String role = getDataFromResponse(response,"role");

        return chatMessageDTOMapper.mapToChatMassageDTO(role,message);
    }


    private String getDataFromResponse(String json, String dataRequested) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        String data = objectMapper.treeToValue(
                root.path("choices")
                        .path(0).path("message")
                        .path(dataRequested), String.class);
        return data;
    }

    private HttpEntity<String> createRequestEntity(TelegramRequestDTO telegramRequestDTO) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", Constants.AUTHORIZATION_KEY);

        requestBody = chatCompletionDTOMapper
                .mapToDTO(telegramRequestDTO, OpenAiModels.CHAT_COMPLETION)
                .getModelAsJSON();

        logger.info("----------chatCompletionDTO : " + requestBody + "----------");//TODO: Create a template

        return new HttpEntity<String>(requestBody.toString(), headers);
    }
}
