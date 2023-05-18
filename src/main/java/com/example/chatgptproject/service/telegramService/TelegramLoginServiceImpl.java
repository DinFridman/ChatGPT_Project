package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.dto.LoginUserDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.model.UserSessionDetails;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramLoginServiceImpl implements TelegramLoginService{
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    private final TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    private final AuthService authService;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
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
            return createTelegramResponseDTO(
                    chatId, TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE);

        telegramUserStateServiceImpl.setUsernameToUserSession(username,chatId);

        return createTelegramResponseDTO(chatId,
                TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE);
    }

    private Boolean checkIfUsernameNotExists(String username) {
        return !authService.checkIfAppUserExists(username);
    }

    private TelegramResponse createTelegramResponseWithMainChatKeyboard(Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithChatMainKeyboard(chatId,text);
    }

    private TelegramMessageResponseDTO createTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {

        telegramUserStateServiceImpl.setPasswordToUserSession(password,chatId);
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
            return createTelegramResponseDTO(chatId,TELEGRAM_INCORRECT_PASSWORD_ENTERED);
        }

        log.info("username : {} logged in successfully.", userSessionDetails.getUsername());

        telegramUserStateServiceImpl.turnOffLogInState(chatId);

        return createTelegramResponseWithMainChatKeyboard(chatId,
                TELEGRAM_USER_LOGGED_IN_SUCCESSFULLY_RESPONSE);
    }

    private UserSessionDetails getUserSessionDetailsByChatId(Long chatId) {
        return telegramUserStateServiceImpl.getUserSessionDetails(chatId);
    }

    private LoginUserDTO mapUserSessionDetailsToLoginUserDTO(UserSessionDetails userSessionDetails) {
        return loginUserDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                userSessionDetails.getPassword());
    }
}
