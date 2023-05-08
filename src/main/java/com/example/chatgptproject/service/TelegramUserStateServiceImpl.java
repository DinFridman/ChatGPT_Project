package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.UserSessionDetails;
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
        if(checkIfUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnRegisterMode(chatId);

        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    @Override
    public boolean checkIfRegisterRequest(Long chatId) {
        return getUserSessionDetails(chatId).getIsRegisterRequest();
    }

    @Override
    public boolean checkIfUserLoggedIn(Long chatId) {
        return getUserSessionDetails(chatId).getIsLoggedIn();
    }

    @Override
    public UserSessionDetails getUserSessionDetails(Long chatId) {
        return telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId);
    }

    private TelegramResponse handleUnAuthorizedRequestWhenLoggedIn(Long chatId) {
        return getTelegramResponseDTO(chatId,"You are logged in. \n" +
                "Please logout and try again.");
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void turnOnRegisterMode(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        setIsRegisterRequestOn(userSessionDetails);
        updateUserSession(userSessionDetails);

        log.info("user : {} is on register request mode.", userSessionDetails.getUsername());
    }

    private void setIsRegisterRequestOn(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsRegisterRequest(true);
    }

    @Override
    public TelegramResponse startLoginState(Long chatId) {

        if(checkIfUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnLoginMode(chatId);

        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    private void turnOnLoginMode(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsLoginRequest(true);
        updateUserSession(userSessionDetails);
        log.info("user : {} is on login request mode.", userSessionDetails.getUsername());
    }

    @Override
    public boolean checkIfUsernameHasBeenSet(Long chatId) {
        return getUserSessionDetails(chatId).getUsername() != null;
    }

    @Override
    public void setUsernameToUserSession(String username, Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setUsername(username);
        updateUserSession(userSessionDetails);

        log.info("username has been set to user successfully.");
    }

    private void updateUserSession(UserSessionDetails userSessionDetails) {
        telegramUsersSessionServiceImpl.updateUserSessionDetails(userSessionDetails);
    }

    @Override
    public void setPasswordToUserSession(String password, Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setPassword(password);
        updateUserSession(userSessionDetails);

        log.info("password has been set to user successfully.");
    }

    @Override
    public void turnOffRegistrationState(Long chatId) {
        turnOffUserRegistrationMode(chatId);
        resetUsernameAndPasswordToUserLoginDTO(chatId);
    }

    private void turnOffUserRegistrationMode(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsRegisterRequest(false);
        updateUserSession(userSessionDetails);
    }

    private void resetUsernameAndPasswordToUserLoginDTO(Long chatId) {
        setPasswordToUserSession(null,chatId);
        setUsernameToUserSession(null,chatId);
    }

    @Override
    public void turnOffLogInState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        turnOnUserLoggedInState(userSessionDetails);
        turnOffUserLoginRequestState(userSessionDetails);
        telegramUsersSessionServiceImpl.updateUserSessionDetails(userSessionDetails);
    }

    private void turnOnUserLoggedInState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoggedIn(true);
    }

    private void turnOffUserLoginRequestState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoginRequest(false);
    }

    @Override
    public void handleStartingChatState(Long chatId) {
        createNewSessionForUser(chatId);
    }

    @Override
    public boolean checkIfUserHasSession(Long chatId) {
        return telegramUsersSessionServiceImpl.checkIfUserSessionExistByChatId(chatId);
    }

    private void createNewSessionForUser(Long chatId) {
        telegramUsersSessionServiceImpl.createNewSessionForUser(chatId);
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
                "Please provide an email to send your conversation.");
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
