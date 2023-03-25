package com.example.chatgptproject.bot;

import com.example.chatgptproject.utils.Constants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class TelegramBot {

    public void sendMessage(String chatId, String text) {
        String url = "https://api.telegram.org/bot"
                + Constants.TELEGRAM_BOT_TOKEN
                + "/sendMessage";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = "{\"chat_id\":\"" + chatId + "\",\"text\":\"" + text + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, entity, String.class);
    }

    public void sendLoginMessage(Long chatId) {
        sendMessage(chatId.toString(),
                "Please enter your password to register or login."
                        + " send /register or /login and then your password.");
    }

    public void sendRegisteredMessage(Long chatId) {
        sendMessage(chatId.toString(), "user registered successfully!");
    }

    public void sendLoggedInMessage(Long chatId) {
        sendMessage(chatId.toString(), "user logged in successfully!");
    }

    public String getBotUsername() {
        return Constants.TELEGRAM_BOT_NAME;
    }

    public String getBotToken() {
        return Constants.TELEGRAM_BOT_TOKEN;
    }
}
