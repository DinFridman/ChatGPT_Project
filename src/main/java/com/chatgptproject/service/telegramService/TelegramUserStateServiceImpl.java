package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramMessageResponseDTO;
import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.chatgptproject.model.UserSessionDetails;
import com.chatgptproject.utils.constants.TelegramResponseConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramUserStateServiceImpl implements TelegramUserStateService{
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramUsersSessionServiceImpl telegramUsersSessionServiceImpl;

    @Override
    public TelegramResponse startRegisterState(Long chatId) {
        if(isUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnRegisterMode(chatId);

        return getTelegramResponseDTO(chatId, TelegramResponseConstants.TELEGRAM_ENTER_USERNAME_MESSAGE);
    }

    @Override
    public boolean isRegisterRequest(Long chatId) {
        return getUserSessionDetails(chatId).getIsRegisterRequest();
    }

    @Override
    public boolean isUserLoggedIn(Long chatId) {
        return getUserSessionDetails(chatId).getIsLoggedIn();
    }

    @Override
    public UserSessionDetails getUserSessionDetails(Long chatId) {
        return telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId);
    }

    private TelegramResponse handleUnAuthorizedRequestWhenLoggedIn(Long chatId) {
        return getTelegramResponseDTO(chatId, TelegramResponseConstants.TELEGRAM_USER_LOGGED_IN_ALREADY);
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void turnOnRegisterMode(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        setIsRegisterRequestOn(userSessionDetails);
        turnOffUserLoginRequestState(userSessionDetails);
        resetUsernameAndPasswordToUserSessionDetails(userSessionDetails);
        updateUserSession(userSessionDetails);

        log.info("user : {} is on register request mode.", userSessionDetails.getUsername());
    }

    private void setIsRegisterRequestOn(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsRegisterRequest(true);
    }

    @Override
    public TelegramResponse startLoginState(Long chatId) {

        if(isUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnLoginMode(chatId);

        return getTelegramResponseDTO(chatId, TelegramResponseConstants.TELEGRAM_ENTER_USERNAME_MESSAGE);
    }

    private void turnOnLoginMode(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        setIsLoginRequestOn(userSessionDetails);
        resetUsernameAndPasswordToUserSessionDetails(userSessionDetails);
        turnOffRegistrationState(userSessionDetails);
        updateUserSession(userSessionDetails);
        log.info("user : {} is on login request mode.", userSessionDetails.getUsername());
    }

    private void setIsLoginRequestOn(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoginRequest(true);
    }

    @Override
    public boolean isUsernameProvided(Long chatId) {
        return getUserSessionDetails(chatId).getUsername() != null;
    }

    @Override
    public void setUsernameToUserSession(String username, UserSessionDetails userSessionDetails) {
        userSessionDetails.setUsername(username);
        updateUserSession(userSessionDetails);

        log.info("username has been set to user successfully.");
    }

    private void updateUserSession(UserSessionDetails userSessionDetails) {
        telegramUsersSessionServiceImpl.updateUserSessionDetails(userSessionDetails);
    }

    @Override
    public void setPasswordToUserSession(String password, UserSessionDetails userSessionDetails) {
        userSessionDetails.setPassword(password);
        updateUserSession(userSessionDetails);

        log.info("password has been set to user successfully.");
    }

    @Override
    public void turnOffRegistrationState(UserSessionDetails userSessionDetails) {
        turnOffUserRegistrationMode(userSessionDetails);
    }

    private void turnOffUserRegistrationMode(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsRegisterRequest(false);
        updateUserSession(userSessionDetails);
    }

    private void resetUsernameAndPasswordToUserSessionDetails(UserSessionDetails userSessionDetails) {
        setPasswordToUserSession(null, userSessionDetails);
        setUsernameToUserSession(null, userSessionDetails);
    }

    @Override
    public void turnOnLoggedInState(UserSessionDetails userSessionDetails) {
        turnOnUserLoggedInState(userSessionDetails);
    }

    private void turnOnUserLoggedInState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoggedIn(true);
    }

    private void turnOffUserLoginRequestState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoginRequest(false);
    }

    @Override
    public void handleSuccessfulLogin(UserSessionDetails userSessionDetails) {
        turnOnLoggedInState(userSessionDetails);
        turnOffUserLoginRequestState(userSessionDetails);
        updateUserSession(userSessionDetails);
    }

    @Override
    public void handleStartingChatState(Long chatId) {
        createNewSessionForUser(chatId);
    }

    @Override
    public boolean checkIfUserHasSession(Long chatId) {
        return telegramUsersSessionServiceImpl.checkIfUserSessionExistsByChatId(chatId);
    }

    private void createNewSessionForUser(Long chatId) {
        UserSessionDetails userSessionDetails = telegramUsersSessionServiceImpl.createUserSessionDetailsFromChatId(chatId);
        telegramUsersSessionServiceImpl.createNewSessionForUser(userSessionDetails);
    }

    @Override
    public void handleLogoutRequest(Long chatId) {
        startLogoutState(chatId);
    }

    private void startLogoutState(Long chatId) {
        telegramUsersSessionServiceImpl.removeUserSessionDetails(chatId);
    }

    @Override
    public boolean checkIfLoginRequest(Long chatId) {
        return (getUserSessionDetails(chatId).getIsLoginRequest());
    }

    @Override
    public String getUsernameFromUserSessionByChatId(Long chatId) {
        return getUserSessionDetails(chatId).getUsername();
    }

    @Override
    public TelegramResponse startSendConversationState(Long chatId) {

        turnOnEmailConversationState(chatId);

        return getTelegramResponseDTO(chatId,
                TelegramResponseConstants.TELEGRAM_ENTER_EMAIL_MESSAGE);
    }

    @Override
    public boolean checkIfEmailConversationStateOn(Long chatId) {
        return getUserSessionDetails(chatId).getIsEmailConversationRequest();
    }

    @Override
    public void turnOnEmailConversationState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsEmailConversationRequest(true);
        updateUserSession(userSessionDetails);
    }

    @Override
    public void turnOffEmailConversationState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsEmailConversationRequest(false);
        updateUserSession(userSessionDetails);
    }




}
