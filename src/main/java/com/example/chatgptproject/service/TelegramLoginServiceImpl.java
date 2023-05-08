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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return handleUsernameInput(chatId,message);

        handlePasswordInput(chatId,message);
        return loginUserAndGetTelegramResponse(chatId);
    }

    private Boolean isUsernameInputState(Long chatId) {
        return !telegramUserStateServiceImpl.checkIfUsernameHasBeenSet(chatId);
    }

    @Override
    @Transactional
    public TelegramResponse handleUsernameInput(Long chatId, String username) {
        if (checkIfUsernameNotExists(username))
            return createTelegramResponseWithLoginRegisterKeyboard(
                    chatId, TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE);

        telegramUserStateServiceImpl.setUsernameToUserSession(username,chatId);

        return getTelegramResponseDTO(chatId,
                TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE);
    }

    private Boolean checkIfUsernameNotExists(String username) {
        return !authService.checkIfAppUserExists(username);
    }

    private TelegramResponse createTelegramResponseWithMainChatKeyboard(Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithChatMainKeyboard(chatId,text);
    }

    private TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(
            Long chatId, String text) {
        return telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(chatId,text);
    }

    private TelegramResponse createTelegramResponse(Long chatId, String text) {
        return TelegramMessageResponseDTO.builder()
                .text(text)
                .chatId(chatId)
                .build();
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void handlePasswordInput(Long chatId, String password) {

        telegramUserStateServiceImpl.setPasswordToUserSession(password,chatId);
    }

    private void authenticateUser(LoginUserDTO loginUserDTO) {
            authService.loginUser(loginUserDTO);
    }

    @Override
    @Transactional
    public TelegramResponse loginUserAndGetTelegramResponse(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetailsByChatId(chatId);
        LoginUserDTO loginUserDTO = mapUserSessionDetailsToLoginUserDTO(userSessionDetails);

        try {
            authenticateUser(loginUserDTO);
        } catch (BadCredentialsException e) {
            log.info("username : {} failed to log in since password entered is incorrect.",
                    userSessionDetails.getUsername());
            return createTelegramResponse(chatId,TELEGRAM_INCORRECT_PASSWORD_ENTERED);
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
