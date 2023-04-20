package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTO;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayService {
    private final TelegramRegistrationService telegramRegistrationService;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final Map<Long,LoginUserDTO> usersMap = new HashMap<>();
    private final TelegramKeyboardService telegramKeyboardService;
    private final TelegramUserStateService telegramUserStateService;
    private final TelegramLoginService telegramLoginService;

    public TelegramResponse telegramRequestsGateway(@NotNull Update update)
            throws IOException, InterruptedException {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (!messageIsNotEmptyOrNull(message))
            return getTelegramResponseDTO(chatId, "no message!");

        if(!checkIfUserHasSession(chatId))
            return handleStartingChatState(chatId);

        if(checkIfLoginButtonPressed(message))
            return startLoginState(chatId);

        if(checkIfRegisterButtonPressed(message))
            return startRegisterState(chatId);

        if(checkIfLogoutButtonPressed(message))
            return handleLogoutRequest(chatId);

        if (isRegisterRequest(chatId))
            return handleRegisterState(chatId, message);

        if (isLoginRequest(chatId))
            return handleLoginState(chatId,message);

        if(!userIsLoggedIn(chatId))
            return handleStartingChatState(chatId);

        //if(checkIfSendConversationRequest(message))
            //return handleSendConversationRequest(chatId);

        else
            return handleGenerateAnswerState(update);
    }

    private Boolean messageIsNotEmptyOrNull(String message) {
        return (message != null && !message.isEmpty());
    }

    private Boolean checkIfLoginButtonPressed(String message) {
        return message.equals("Login");
    }

    private TelegramResponse startLoginState(Long chatId) {

        return telegramUserStateService.startLoginState(chatId);
    }

    private Boolean checkIfRegisterButtonPressed(String message) {
        return message.equals("Register");
    }

    private TelegramResponse startRegisterState(Long chatId) {
        return telegramUserStateService.startRegisterState(chatId);
    }

    private Boolean checkIfLogoutButtonPressed(String message) {
        return message.equals("Logout");
    }

    private Boolean checkIfSendConversationRequest(String message) {
        return message.equals("Send conversation");
    }

    private void handleSendConversationRequest(Long chatId) {

    }

    private TelegramResponse handleLogoutRequest(Long chatId) {
        telegramUserStateService.handleLogoutRequest(chatId);

        return createTelegramLoginRegisterKeyboardResponse(chatId,
                "Logged out successfully.");
    }

    private Boolean isLoginRequest(Long chatId) {
        return telegramUserStateService.checkIfLoginRequest(chatId);
    }

    private Boolean isRegisterRequest(Long chatId) {
        return telegramUserStateService.checkIfRegisterRequest(chatId);
    }

    private Boolean userIsLoggedIn(Long chatId) {
        return telegramUserStateService.checkIfUserLoggedIn(chatId);
    }

    private Boolean checkIfUserHasSession(Long chatId) {
        return telegramUserStateService.checkIfUserHasSession(chatId);
    }

    private TelegramResponse handleStartingChatState(Long chatId) {
        telegramUserStateService.handleStartingChatState(chatId);

        return createTelegramLoginRegisterKeyboardResponse(chatId,
                "Please login or register.");
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }

    private TelegramResponse handleRegisterState(Long chatId, String message) {
        return telegramRegistrationService.handleRegisterState(chatId,message);
    }

    private TelegramResponse handleLoginState(Long chatId, String message) {
        return telegramLoginService.handleLoginState(chatId,message);
    }

    public TelegramResponse handleGenerateAnswerState(Update update)
            throws IOException, InterruptedException {
        Long chatId = getChatIdFromUpdate(update);
        String username = telegramUserStateService.getUsernameFromMapByChatId(chatId);

        ChatMessageDTO chatMessageDTO = createChatMessageDTO(update,username);

        TelegramMessageResponseDTO openAIResponse = telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);

        String responseMessage = ExtractMessageFromResponse(openAIResponse);

        return telegramKeyboardService.createTelegramResponseWithChatMainKeyboard(
                chatMessageDTO.getChatId(),responseMessage);
    }

    private ChatMessageDTO createChatMessageDTO(Update update, String username) {
        return new ChatMessageDTOMapper().mapToDTO(update, username);
    }

    private String ExtractMessageFromResponse(TelegramMessageResponseDTO telegramMessageResponseDTO) {
        return telegramMessageResponseDTO.getText();
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    public TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }
}
