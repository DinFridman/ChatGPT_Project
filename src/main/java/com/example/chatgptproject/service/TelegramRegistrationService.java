package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.LoginUserDTO;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Log4j2
public class TelegramRegistrationService {
    private final TelegramUserStateService telegramUserStateService;
    private final TelegramKeyboardService telegramKeyboardService;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final RegisterDTOMapper registerDTOMapper;
    private final AuthService authService;


    public TelegramResponse handleRegisterState(Long chatId, String message) {
        if(IsUsernameInputState(chatId))
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        registerUser(chatId);

        return createTelegramLoginRegisterKeyboardResponse(
                chatId,"You have successfully registered.");
    }

    private Boolean IsUsernameInputState(Long chatId) {
        return !telegramUserStateService.checkIfUsernameHasBeenSet(chatId);
    }

    @Transactional
    public TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (checkIfUserExists(username))
            return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, "username is already exists!");

        telegramUserStateService.setUsernameToLoginUserDTO(username,chatId);

        log.info("username : {} entered successfully to registration.", username);

        return getTelegramResponseDTO(chatId,
                "username has successfully entered. Please enter password to register.");
    }

    public Boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {
        telegramUserStateService.setPasswordToLoginUserDTO(password,chatId);
    }

    private void registerUser(Long chatId) {
        LoginUserDTO loginUserDTO = telegramUserStateService.getLoginUserDTOFromUsersMap(chatId);
        authService.registerUser(registerDTOMapper.mapToDTO(
                loginUserDTO.getUsername(),
                loginUserDTO.getPassword()));

        log.info("user : {} registered successfully.", loginUserDTO.getUsername());

        telegramUserStateService.turnOffRegistrationState(chatId);
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }
}
