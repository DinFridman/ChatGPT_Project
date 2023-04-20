package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.LoginUserDTO;
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


    public TelegramResponse handleLoginState(Long chatId, String message) {
        if(isUsernameInputState(chatId))
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        loginUser(chatId);

        return getTelegramResponseDTO(chatId,"User logged in successfully. " +
                "\nYou are now connected to ChatGPT.");
    }

    private Boolean isUsernameInputState(Long chatId) {
        return !telegramUserStateService.checkIfUsernameHasBeenSet(chatId);
    }

    @Transactional
    public TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (!checkIfUserExists(username))
            return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, "Username does not exists!\n" +
                            "Please try again.");

        telegramUserStateService.setUsernameToLoginUserDTO(username,chatId);

        return getTelegramResponseDTO(chatId,
                "Username entered successfully.\n" +
                        "Please enter your password");
    }

    private Boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {
        telegramUserStateService.setPasswordToLoginUserDTO(password,chatId);
    }

    @Transactional
    public void loginUser(Long chatId) {
        LoginUserDTO loginUserDTO = telegramUserStateService.getLoginUserDTOFromUsersMap(chatId);
        authService.loginUser(loginUserDTO);

        log.info("username : {} logged in successfully.", loginUserDTO.getUsername());

        telegramUserStateService.turnOffLogInState(chatId);
    }
}
