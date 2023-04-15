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
    private final AuthService authService;
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

    private LoginUserDTO getLoginUserDTOFromUsersMap(Long chatId) {
        return usersMap.get(chatId);
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
        startLogoutState(chatId);
        return createTelegramLoginRegisterKeyboardResponse(chatId,
                "Logged out successfully.");
    }

    private void startLogoutState(Long chatId) {
        turnOffLoggedInMode(chatId);
    }

    private void turnOffLoggedInMode(Long chatId) {
        getLoginUserDTOFromUsersMap(chatId).setIsLoggedIn(false);
    }

    private Boolean isLoginRequest(Long chatId) {
        return (getLoginUserDTOFromUsersMap(chatId).getIsLoginRequest());
    }

    private Boolean isRegisterRequest(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsRegisterRequest();
    }

    private Boolean userIsLoggedIn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsLoggedIn();
    }

    private Boolean checkIfUserHasSession(Long chatId) {
        return usersMap.containsKey(chatId);
    }

    private void createNewSessionForUser(Long chatId) {
        LoginUserDTO loginUserDTO = createLoginUserDTO();

        addUserToUsersMap(loginUserDTO,chatId);

        log.info("New session created for user : {}", loginUserDTO.getUsername());
        log.debug("loginUserDTO : {}",loginUserDTO);
    }

    private TelegramResponse handleStartingChatState(Long chatId) {
        createNewSessionForUser(chatId);

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
        String username = getUsernameFromLoginUserMap(chatId);
        ChatMessageDTO chatMessageDTO = new ChatMessageDTOMapper().mapToDTO(update, username);
        TelegramMessageResponseDTO telegramResponse = telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);
        String responseMessage = telegramResponse.getText();

        return telegramKeyboardService.createTelegramResponseWithChatMainKeyboard(
                chatMessageDTO.getChatId(),responseMessage);
    }

    private String getUsernameFromLoginUserMap(Long chatId) {
       return getLoginUserDTOFromUsersMap(chatId).getUsername();
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    private String extractMetadataFromLoginMessage(String message, String subString) {
        return message.substring(subString.length()).trim();
    }

    private void addUserToUsersMap(LoginUserDTO loginUserDTO, Long chatId) {
        usersMap.put(chatId,loginUserDTO);
    }

    private LoginUserDTO createLoginUserDTO() {
        return new LoginUserDTO();
    }

    public TelegramResponse performLogout(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsLoggedIn(false);

        return createTelegramLoginRegisterKeyboardResponse(chatId, "user logged out successfully!");
    }

    public TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }
}
