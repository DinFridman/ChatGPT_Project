package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.model.UserSessionDetails;
import com.example.chatgptproject.security.dto.RegisterDTO;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.*;

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
    @Transactional
    public TelegramResponse handleRegisterState(Long chatId, String message) {
        if(IsUsernameInputState(chatId))
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        registerUser(chatId);

        return createTelegramLoginRegisterKeyboardResponse(
                chatId,TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE);
    }

    private boolean IsUsernameInputState(Long chatId) {
        return !telegramUserStateServiceImpl.isUsernameProvided(chatId);
    }


    private TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (checkIfUserExists(username))
            return createTelegramResponseDTO(chatId,TELEGRAM_USERNAME_EXISTS_MESSAGE);

        telegramUserStateServiceImpl.setUsernameToUserSession(username,chatId);

        log.info("username : {} entered successfully to registration.", username);

        return createTelegramResponseDTO(chatId,
                TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE);
    }

    private boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private TelegramMessageResponseDTO createTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {
        telegramUserStateServiceImpl.setPasswordToUserSession(password,chatId);
    }

    private void registerUser(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        RegisterDTO registerDTO = createRegisterDTOFromUserSessionDetails(userSessionDetails);
        authService.registerUser(registerDTO);

        log.info("user : {} registered successfully.", userSessionDetails.getUsername());

        turnOffRegistrationState(chatId);
    }

    private UserSessionDetails getUserSessionDetails(Long chatId) {
        return telegramUserStateServiceImpl.getUserSessionDetails(chatId);
    }

    private RegisterDTO createRegisterDTOFromUserSessionDetails(
            UserSessionDetails userSessionDetails) {
        return registerDTOMapper.mapToDTO(
                userSessionDetails.getUsername(),
                userSessionDetails.getPassword());
    }

    private void turnOffRegistrationState(Long chatId) {
        telegramUserStateServiceImpl.turnOffRegistrationState(chatId);
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }
}
