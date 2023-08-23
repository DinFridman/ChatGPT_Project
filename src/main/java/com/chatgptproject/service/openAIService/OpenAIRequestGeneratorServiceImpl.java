package com.chatgptproject.service.openAIService;

import com.chatgptproject.dto.mapper.OpenAIRequestDTOMapper;
import com.chatgptproject.utils.enums.OpenAiModels;
import com.chatgptproject.dto.ConversationDTO;
import com.chatgptproject.dto.openAI.OpenAIRequestDTO;
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

import static com.chatgptproject.utils.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class OpenAIRequestGeneratorServiceImpl implements OpenAIRequestGeneratorService{
    private final ObjectMapper objectMapper;
    private final OpenAIRequestDTOMapper openAIRequestDTOMapper;
    @Value("${OPEN_AI_KEY}") String openAiKey;


    @Override
    public String createOpenAICompletion(ConversationDTO conversationDTO)
            throws IOException, InterruptedException {
        OpenAIRequestDTO request = createOpenAIRequestDTO(conversationDTO);

        log.info("----------AI_Request : " + request + "----------");

        return sendChatgptRequest(objectMapper.writeValueAsString(request));
    }

    @Override
    public String sendChatgptRequest(String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = createHttpRequest(body);
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    @Override
    public HttpRequest createHttpRequest(String body) {
        return HttpRequest.newBuilder().uri(URI.create(CHAT_COMPLETION_URL))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    @Override
    public OpenAIRequestDTO createOpenAIRequestDTO(ConversationDTO conversationDTO) {
        return openAIRequestDTOMapper.mapToDTO(
                OpenAiModels.CHAT_COMPLETION_GPT_3,
                conversationDTO,
                OPEN_AI_MAX_TOKENS,
                OPEN_AI_TEMPERATURE);
    }
}
