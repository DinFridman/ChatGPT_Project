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
public class TelegramUserStateService {
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramUsersSessionService telegramUsersSessionService;

    public TelegramResponse startRegisterState(Long chatId) {
        if(checkIfUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnRegisterMode(chatId);

        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    public Boolean checkIfRegisterRequest(Long chatId) {
        return getUserSessionDetails(chatId).getIsRegisterRequest();
    }

    public Boolean checkIfUserLoggedIn(Long chatId) {
        return getUserSessionDetails(chatId).getIsLoggedIn();
    }

    public UserSessionDetails getUserSessionDetails(Long chatId) {
        return telegramUsersSessionService.getUserSessionDetailsFromRepo(chatId);
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

    public Boolean checkIfUsernameHasBeenSet(Long chatId) {
        return getUserSessionDetails(chatId).getUsername() != null;
    }

    public void setUsernameToUserSession(String username, Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setUsername(username);
        updateUserSession(userSessionDetails);

        log.info("username has been set to user successfully.");
    }

    private void updateUserSession(UserSessionDetails userSessionDetails) {
        telegramUsersSessionService.updateUserSessionDetails(userSessionDetails);
    }

    public void setPasswordToUserSession(String password, Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setPassword(password);
        updateUserSession(userSessionDetails);

        log.info("password has been set to user successfully.");
    }

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

    public void turnOffLogInState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        turnOnUserLoggedInState(userSessionDetails);
        turnOffUserLoginRequestState(userSessionDetails);
        telegramUsersSessionService.updateUserSessionDetails(userSessionDetails);
    }

    private void turnOnUserLoggedInState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoggedIn(true);
    }

    private void turnOffUserLoginRequestState(UserSessionDetails userSessionDetails) {
        userSessionDetails.setIsLoginRequest(false);
    }

    public void handleStartingChatState(Long chatId) {
        createNewSessionForUser(chatId);
    }

    public Boolean checkIfUserHasSession(Long chatId) {
        return telegramUsersSessionService.checkIfUserSessionExistByChatId(chatId);
    }

    public void createNewSessionForUser(Long chatId) {
        telegramUsersSessionService.createNewSessionForUser(chatId);
    }

    public void handleLogoutRequest(Long chatId) {
        startLogoutState(chatId);
    }

    private void startLogoutState(Long chatId) {
        telegramUsersSessionService.removeUserSessionDetails(chatId);
    }

    public Boolean checkIfLoginRequest(Long chatId) {
        return (getUserSessionDetails(chatId).getIsLoginRequest());
    }

    public String getUsernameFromUserSessionByChatId(Long chatId) {
        return getUserSessionDetails(chatId).getUsername();
    }

    public TelegramResponse startSendConversationState(Long chatId) {

        turnOnEmailConversationState(chatId);

        return getTelegramResponseDTO(chatId,
                "Please provide an email to send your conversation.");
    }

    public Boolean checkIfEmailConversationStateOn(Long chatId) {
        return getUserSessionDetails(chatId).getIsEmailConversationRequest();
    }

    public void turnOnEmailConversationState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsEmailConversationRequest(true);
        updateUserSession(userSessionDetails);
    }

    public void turnOffEmailConversationState(Long chatId) {
        UserSessionDetails userSessionDetails = getUserSessionDetails(chatId);
        userSessionDetails.setIsEmailConversationRequest(false);
        updateUserSession(userSessionDetails);
    }




}
