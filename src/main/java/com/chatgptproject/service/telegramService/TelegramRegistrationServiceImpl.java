package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.chatgptproject.security.dto.RegisterDTOMapper;
import com.chatgptproject.security.service.AuthService;
import com.chatgptproject.model.UserSessionDetails;
import com.chatgptproject.security.dto.RegisterDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chatgptproject.utils.constants.TelegramResponseConstants.*;

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
            return createTelegramResponseLoginRegisterKeyboard(chatId,TELEGRAM_USERNAME_EXISTS_MESSAGE);

        setUsernameInput(chatId,username);

        log.info("username : {} entered successfully to registration mode.", username);

        return createTelegramResponseLoginRegisterKeyboard(chatId,
                TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE);
    }

    private void setUsernameInput(Long chatId, String username) {
        UserSessionDetails userSessionDetails = telegramUserStateServiceImpl.getUserSessionDetails(chatId);
        telegramUserStateServiceImpl.setUsernameToUserSession(username,userSessionDetails);
    }

    private boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private TelegramResponse createTelegramResponseLoginRegisterKeyboard(Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(chatId,text);
    }

    private void handlePasswordInput(Long chatId, String password) {
        setPasswordInput(chatId,password);
    }

    private void setPasswordInput(Long chatId, String password) {
        UserSessionDetails userSessionDetails = telegramUserStateServiceImpl.getUserSessionDetails(chatId);
        telegramUserStateServiceImpl.setPasswordToUserSession(password,userSessionDetails);
    }


    private void registerUser(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        RegisterDTO registerDTO = createRegisterDTOFromUserSessionDetails(userSessionDetails);
        authService.registerUser(registerDTO);

        log.info("user : {} registered successfully.", userSessionDetails.getUsername());

        turnOffRegistrationState(userSessionDetails);
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

    private void turnOffRegistrationState(UserSessionDetails userSessionDetails) {
        telegramUserStateServiceImpl.turnOffRegistrationState(userSessionDetails);
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }
}
