package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.UserSessionDetails;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE;

@RequiredArgsConstructor
@Service
@Log4j2
public class TelegramRegistrationServiceImpl implements TelegramRegistrationService {
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    private final TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final RegisterDTOMapper registerDTOMapper;
    private final AuthService authService;

    @Override
    public TelegramResponse handleRegisterState(Long chatId, String message) {
        if(IsUsernameInputState(chatId))
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        registerUser(chatId);

        return createTelegramLoginRegisterKeyboardResponse(
                chatId,TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE);
    }

    private Boolean IsUsernameInputState(Long chatId) {
        return !telegramUserStateServiceImpl.checkIfUsernameHasBeenSet(chatId);
    }

    @Override
    @Transactional
    public TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (checkIfUserExists(username))
            return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, "username is already exists!");

        telegramUserStateServiceImpl.setUsernameToUserSession(username,chatId);

        log.info("username : {} entered successfully to registration.", username);

        return getTelegramResponseDTO(chatId,
                "username has successfully entered. Please enter password to register.");
    }

    @Override
    public boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {
        telegramUserStateServiceImpl.setPasswordToUserSession(password,chatId);
    }

    private void registerUser(Long chatId) {
        UserSessionDetails userSessionDetails = telegramUserStateServiceImpl.getUserSessionDetails(chatId);
        authService.registerUser(registerDTOMapper.mapToDTO(
                userSessionDetails.getUsername(),
                userSessionDetails.getPassword()));

        log.info("user : {} registered successfully.", userSessionDetails.getUsername());

        telegramUserStateServiceImpl.turnOffRegistrationState(chatId);
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }
}