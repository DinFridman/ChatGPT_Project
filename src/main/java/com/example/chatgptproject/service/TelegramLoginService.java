package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.LoginUserDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.UserSessionDetails;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramLoginService {
    private final TelegramUserStateService telegramUserStateService;
    private final TelegramKeyboardService telegramKeyboardService;
    private final AuthService authService;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final LoginUserDTOMapper loginUserDTOMapper;


    public TelegramResponse handleLoginState(Long chatId, String message) {
        if(isUsernameInputState(chatId))
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        loginUser(chatId);

        return createTelegramResponseWithMainChatKeyboard(chatId,
                "User logged in successfully. " +
                "\nYou are now connected to ChatGPT.");
    }

    private Boolean isUsernameInputState(Long chatId) {
        return !telegramUserStateService.checkIfUsernameHasBeenSet(chatId);
    }

    @Transactional
    public TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (checkIfUsernameNotExists(username))
            return createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, "Username does not exists!\n" +
                            "Please try again.");

        telegramUserStateService.setUsernameToUserSession(username,chatId);

        return getTelegramResponseDTO(chatId,
                "Username entered successfully.\n" +
                        "Please enter your password.");
    }

    private Boolean checkIfUsernameNotExists(String username) {
        return !authService.checkIfAppUserExists(username);
    }

    private TelegramResponse createTelegramResponseWithMainChatKeyboard(Long chatId, String text) {
        return telegramKeyboardService.createTelegramResponseWithChatMainKeyboard(chatId,text);
    }

    private TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(
            Long chatId, String text) {
        return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(chatId,text);
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {
        telegramUserStateService.setPasswordToUserSession(password,chatId);
    }

    @Transactional
    public void loginUser(Long chatId) {
        UserSessionDetails userSessionDetails =
                telegramUserStateService.getUserSessionDetails(chatId);
        LoginUserDTO loginUserDTO = loginUserDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                userSessionDetails.getPassword());

        authService.loginUser(loginUserDTO);

        log.info("username : {} logged in successfully.", userSessionDetails.getUsername());

        telegramUserStateService.turnOffLogInState(chatId);
    }
}
