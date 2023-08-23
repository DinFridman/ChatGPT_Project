package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.chatgptproject.model.UserSessionDetails;
import com.chatgptproject.security.service.AuthService;
import com.chatgptproject.utils.constants.TelegramResponseConstants;
import com.chatgptproject.dto.LoginUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramLoginServiceImpl implements TelegramLoginService{
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    private final TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    private final AuthService authService;
    private final LoginUserDTOMapper loginUserDTOMapper;

    @Override
    public TelegramResponse handleLoginState(Long chatId, String message) {
        if(isUsernameInputState(chatId))
            return handleUsernameInputAndGetTelegramResponse(chatId,message);

        handlePasswordInput(chatId,message);
        return loginUserAndGetTelegramResponse(chatId);
    }

    private boolean isUsernameInputState(Long chatId) {
        return !telegramUserStateServiceImpl.isUsernameProvided(chatId);
    }

    private TelegramResponse handleUsernameInputAndGetTelegramResponse(Long chatId, String username) {
        if (checkIfUsernameNotExists(username))
            return createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, TelegramResponseConstants.TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE);

        UserSessionDetails userSessionDetails = getUserSessionDetailsByChatId(chatId);
        telegramUserStateServiceImpl.setUsernameToUserSession(username,userSessionDetails);

        return createTelegramResponseWithLoginRegisterKeyboard(chatId,
                TelegramResponseConstants.TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                "\n" + TelegramResponseConstants.TELEGRAM_ENTER_PASSWORD_MESSAGE);
    }

    private Boolean checkIfUsernameNotExists(String username) {
        return !authService.checkIfAppUserExists(username);
    }

    private TelegramResponse createTelegramResponseWithMainChatKeyboard(Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithChatMainKeyboard(chatId,text);
    }

    private TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(chatId,text);
    }

    private void handlePasswordInput(Long chatId, String password) {
        UserSessionDetails userSessionDetails = getUserSessionDetailsByChatId(chatId);
        telegramUserStateServiceImpl.setPasswordToUserSession(password, userSessionDetails);
    }

    private void authenticateUser(LoginUserDTO loginUserDTO) {
            authService.loginUser(loginUserDTO);
    }

    private TelegramResponse loginUserAndGetTelegramResponse(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetailsByChatId(chatId);
        LoginUserDTO loginUserDTO = mapUserSessionDetailsToLoginUserDTO(userSessionDetails);

        try {
            authenticateUser(loginUserDTO);
        } catch (BadCredentialsException e) {
            log.info("username : {} failed to login since password provided is incorrect.",
                    userSessionDetails.getUsername());
            return createTelegramResponseWithLoginRegisterKeyboard(chatId, TelegramResponseConstants.TELEGRAM_INCORRECT_PASSWORD_ENTERED);
        }

        log.info("username : {} logged in successfully.", userSessionDetails.getUsername());

        telegramUserStateServiceImpl.handleSuccessfulLogin(userSessionDetails);

        return createTelegramResponseWithMainChatKeyboard(chatId,
                TelegramResponseConstants.TELEGRAM_USER_LOGGED_IN_SUCCESSFULLY_RESPONSE);
    }

    private UserSessionDetails getUserSessionDetailsByChatId(Long chatId) {
        return telegramUserStateServiceImpl.getUserSessionDetails(chatId);
    }

    private LoginUserDTO mapUserSessionDetailsToLoginUserDTO(UserSessionDetails userSessionDetails) {
        return loginUserDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                userSessionDetails.getPassword());
    }
}
