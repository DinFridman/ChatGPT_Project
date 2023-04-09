package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTOMapper;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayService {
    private final AuthService authService;
    private final RegisterDTOMapper registerDTOMapper;
    private final LoginUserDTOMapper loginUserDTOMapper;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;

    public TelegramResponseDTO telegramRequestsGateway(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        String message = chatMessageDTO.getMessage();
        Long chatId = chatMessageDTO.getChatId();

        if (message != null && !message.isEmpty()) {
            if (message.startsWith("/start")) {
                return handleStartingState(chatId);
            } else if (message.startsWith("/register")) {
                return handleRegisterState(chatId, message);
            } else if (message.startsWith("/login")) {
                return handleLoginState(chatId, message);
            } else {
                return handleChatState(chatMessageDTO);
            }
        }
        return getTelegramResponseDTO(chatId, "no message!");
    }

    public TelegramResponseDTO handleChatState(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        return telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);
    }

        public TelegramResponseDTO handleStartingState(Long chatId) {
        return getTelegramResponseDTO(chatId,
                "Please enter your password to register or login."
                        + " send /register or /login and then your password.");
    }

    public TelegramResponseDTO handleRegisterState(Long chatId, String message) {
        String password = message.substring("/register".length()).trim();
        authService.registerUser(registerDTOMapper.mapToDTO(chatId.toString(),password));
        return getTelegramResponseDTO(chatId,
                "user saved successfully!");
    }

    public TelegramResponseDTO handleLoginState(Long chatId, String message) {
        String password = message.substring("/login".length()).trim();
        ResponseCookie cookie = authService.loginUser(
                loginUserDTOMapper.mapToDTO(chatId.toString(),password));

        return getTelegramResponseDTO(chatId,"user logged in successfully!");

       /* return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseBody);*/
    }

    public TelegramResponseDTO handleLogoutState(Long chatId, String message) {
        String password = message.substring("/logout".length()).trim();
        ResponseCookie cookie = authService.logoutUser();

        return getTelegramResponseDTO(chatId,"user logged out successfully!");
    }



    public TelegramResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }


    public Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

}
