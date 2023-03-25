package com.example.chatgptproject.service;

import com.example.chatgptproject.bot.TelegramBot;
import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTOMapper;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayService {
    private final AuthService authService;
    private TelegramBot telegramBot = new TelegramBot();
    private final RegisterDTOMapper registerDTOMapper;
    private final LoginUserDTOMapper loginUserDTOMapper;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestHandler telegramRequestHandler;
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    public ResponseEntity<TelegramResponseDTO> telegramRequestsGateway(Update update)
            throws IOException, InterruptedException {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            String message = getMessageFromUpdate(update);
            Long chatId = getChatIdFromUpdate(update);
            if (message.equals("/start")) {
                return handleStartingState(chatId);
            } else if (message.startsWith("/register")) {
                return handleRegisterState(chatId, message);
            } else if (message.startsWith("/login")) {
                return handleLoginState(chatId, message);
            } else {
                return handleChatState(update);
            }
        }
            return new ResponseEntity<>(
                    getTelegramResponseDTO(getChatIdFromUpdate(update), "no message!"),
                    HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<TelegramResponseDTO> handleChatState(Update request)
            throws IOException, InterruptedException {
        TelegramResponseDTO response = telegramRequestHandler
                .handleTelegramRequest(chatMessageDTOMapper.mapToDTO(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<TelegramResponseDTO> handleStartingState(Long chatId) {
        TelegramResponseDTO responseBody =
                getTelegramResponseDTO(chatId,
                        "Please enter your password to register or login."
                + " send /register or /login and then your password.");
        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<TelegramResponseDTO> handleRegisterState(Long chatId, String message) {
        String password = message.substring("/register".length()).trim();
        authService.registerUser(registerDTOMapper.mapToDTO(chatId.toString(),password));
        TelegramResponseDTO responseBody =
                getTelegramResponseDTO(chatId,"user saved successfully!");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public ResponseEntity<TelegramResponseDTO> handleLoginState(Long chatId, String message) {
        String password = message.substring("/login".length()).trim();
        ResponseCookie cookie = authService.loginUser(
                loginUserDTOMapper.mapToDTO(chatId.toString(),password));

        TelegramResponseDTO responseBody =
                getTelegramResponseDTO(chatId,"user logged in successfully!");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseBody);
    }

    public TelegramResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    public String getMessageFromUpdate(Update update) {
        return update.getMessage().getText();
    }

    public Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

}
