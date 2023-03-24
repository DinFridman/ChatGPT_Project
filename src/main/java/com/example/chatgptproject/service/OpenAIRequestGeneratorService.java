package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.mapper.OpenAIRequestDTOMapper;
import com.example.chatgptproject.dto.openAI.OpenAIRequestDTO;
import com.example.chatgptproject.utils.enums.OpenAiModels;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class OpenAIRequestGeneratorService {
    private final ObjectMapper objectMapper;
    private final OpenAIRequestDTOMapper openAIRequestDTOMapper;
    @Value("${openai.key}")
    private String openAiAuthKey;
    @Value("${chatcompletion.url}")
    private String chatCompletionURL;
    @Value("${openai.maxtokens}")
    private int max_tokens;
    @Value("${openai.temperature}")
    private double temperature;


    public String createOpenAICompletion(ConversationDTO conversationDTO)
            throws Exception {
        OpenAIRequestDTO request = createOpenAIRequestDTO(conversationDTO);
        log.info("----------AI_Request : " + request + "----------");
        return sendChatgptRequest(objectMapper.writeValueAsString(request));
    }

    public String sendChatgptRequest(String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = createHttpRequest(body);
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public HttpRequest createHttpRequest(String body) {
        return HttpRequest.newBuilder().uri(URI.create(chatCompletionURL))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiAuthKey)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    public OpenAIRequestDTO createOpenAIRequestDTO(ConversationDTO conversationDTO) {
        return openAIRequestDTOMapper.mapToDTO(
                OpenAiModels.CHAT_COMPLETION,
                conversationDTO,
                max_tokens,
                temperature);
    }
}
