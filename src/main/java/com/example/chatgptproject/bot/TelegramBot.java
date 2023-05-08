package com.example.chatgptproject.bot;

import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.service.TelegramGatewayServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.example.chatgptproject.utils.constants.Constants.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class TelegramBot extends TelegramLongPollingBot {
    private final ObjectMapper objectMapper;
    private final TelegramGatewayServiceImpl telegramGatewayServiceImpl;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            TelegramResponse telegramMessageResponseDTO;
            try {
                telegramMessageResponseDTO =
                        telegramGatewayServiceImpl.telegramRequestsGateway(update);

                sendTelegramMessage(telegramMessageResponseDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTelegramMessage(TelegramResponse telegramResponse)
            throws IOException, InterruptedException {

        String body = getTelegramTextMessageAsString(telegramResponse);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest telegramRequest = createTelegramHttpRequest(body);

        log.info("telegram Response sent successfully.");

        String response = client.send(telegramRequest, HttpResponse.BodyHandlers.ofString()).body();
        log.info("-----------------------" +
                "Telegram Server Response: {} " +
                "-----------------------", response);
    }

    public HttpRequest createTelegramHttpRequest(String body) {
        URI uri = createTelegramRequestURI();
        return HttpRequest.newBuilder().uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    private String getTelegramTextMessageAsString(TelegramResponse telegramResponse)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(telegramResponse);
    }

    private URI createTelegramRequestURI() {
        return URI.create(TELEGRAM_URL + TELEGRAM_BOT_TOKEN + SEND_MESSAGE_URL);
    }

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TELEGRAM_BOT_TOKEN;
    }
}
