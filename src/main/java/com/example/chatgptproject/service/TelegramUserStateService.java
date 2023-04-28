package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.dto.LoginUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import static com.example.chatgptproject.utils.Constants.UNINITIALIZED_VALUE;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramUserStateService {
    private final Map<Long,LoginUserDTO> usersMap = new HashMap<>();
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;



    public TelegramResponse startRegisterState(Long chatId) {

        if(checkIfUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnRegisterModeInLoginUser(chatId);

        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    public Boolean checkIfRegisterRequest(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsRegisterRequest();
    }

    public Boolean checkIfUserLoggedIn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsLoggedIn();
    }

    public LoginUserDTO getLoginUserDTOFromUsersMap(Long chatId) {//TODO: handle null case
        return usersMap.get(chatId);
    }

    private TelegramResponse handleUnAuthorizedRequestWhenLoggedIn(Long chatId) {
        return getTelegramResponseDTO(chatId,"You are logged in. \n" +
                "Please logout and try again.");
    }

    private TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }

    private void turnOnRegisterModeInLoginUser(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        setIsRegisterRequestOn(loginUserDTO);
        addUserToUsersMap(loginUserDTO,chatId);

        log.info("user : {} is on register request mode.", loginUserDTO.getUsername());
    }

    private void setIsRegisterRequestOn(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsRegisterRequest(true);
    }

    public TelegramResponse startLoginState(Long chatId) {

        if(checkIfUserLoggedIn(chatId))
            return handleUnAuthorizedRequestWhenLoggedIn(chatId);

        turnOnLoginModeInLoginUser(chatId);

        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    private void turnOnLoginModeInLoginUser(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsLoginRequest(true);
        addUserToUsersMap(loginUserDTO,chatId);
        log.info("user : {} is on login request mode.", loginUserDTO.getUsername());
    }

    public Boolean checkIfUsernameHasBeenSet(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getUsername() != null;
    }

    public void setUsernameToLoginUserDTO(String username,Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setUsername(username);
        addUserToUsersMap(loginUserDTO,chatId);

        log.info("username has been set to user successfully.");
    }

    private void addUserToUsersMap(LoginUserDTO loginUserDTO, Long chatId) {
        usersMap.put(chatId,loginUserDTO);
    }

    public void setPasswordToLoginUserDTO(String password, Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setPassword(password);
        addUserToUsersMap(loginUserDTO,chatId);

        log.info("password has been set to user successfully.");
    }

    public void turnOffRegistrationState(Long chatId) {
        turnOffUserRegistrationMode(chatId);
        resetUsernameAndPasswordToUserLoginDTO(chatId);
    }

    private void turnOffUserRegistrationMode(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsRegisterRequest(false);
        addUserToUsersMap(loginUserDTO,chatId);
    }

    private void resetUsernameAndPasswordToUserLoginDTO(Long chatId) {
        setPasswordToLoginUserDTO(null,chatId);
        setUsernameToLoginUserDTO(null,chatId);
    }

    public void turnOffLogInState(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        turnOnUserLoggedInState(loginUserDTO);
        turnOffUserLoginRequestState(loginUserDTO);
    }

    private void turnOnUserLoggedInState(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoggedIn(true);
    }

    private void turnOffUserLoginRequestState(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoginRequest(false);
    }

    public void handleStartingChatState(Long chatId) {
        createNewSessionForUser(chatId);
    }

    public Boolean checkIfUserHasSession(Long chatId) {
        return usersMap.containsKey(chatId);
    }

    public void createNewSessionForUser(Long chatId) {
        LoginUserDTO loginUserDTO = createNewLoginUserDTO();

        addUserToUsersMap(loginUserDTO,chatId);

        log.info("-----------------------" +
                "New session created for user : {}" +
                "-----------------------", loginUserDTO.getUsername());
        log.debug("loginUserDTO : {}",loginUserDTO);
    }

    private LoginUserDTO createNewLoginUserDTO() {
        return new LoginUserDTO();
    }

    public void handleLogoutRequest(Long chatId) {
        startLogoutState(chatId);
    }

    private void startLogoutState(Long chatId) {
        usersMap.remove(chatId);
    }

    public Boolean checkIfLoginRequest(Long chatId) {
        return (getLoginUserDTOFromUsersMap(chatId).getIsLoginRequest());
    }

    public String getUsernameFromMapByChatId(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getUsername();
    }

    public TelegramResponse startSendConversationState(Long chatId) {

        turnOnEmailConversationState(chatId);

        return getTelegramResponseDTO(chatId,"Please provide an email to send your conversation.");
    }

    public Boolean checkIfEmailConversationStateOn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsEmailConversationRequest();
    }

    public void turnOnEmailConversationState(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsEmailConversationRequest(true);
        addUserToUsersMap(loginUserDTO,chatId);
    }

    public void turnOffEmailConversationState(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsEmailConversationRequest(false);
        addUserToUsersMap(loginUserDTO,chatId);
    }




}
