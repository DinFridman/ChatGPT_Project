package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTO;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayService {
    private final AuthService authService;
    private final RegisterDTOMapper registerDTOMapper;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final Map<Long,LoginUserDTO> usersMap = new HashMap<>();
    private final TelegramKeyboardService telegramKeyboardService;

    public TelegramResponse telegramRequestsGateway(@NotNull Update update)
            throws IOException, InterruptedException {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (!messageIsNotEmptyOrNull(message))
            return getTelegramResponseDTO(chatId, "no message!");

        if(!checkIfUserHasSession(chatId))
            return handleStartingChatState(chatId);

        if(checkIfLoginButtonPressed(message))
            return startLoginState(chatId);

        if(checkIfRegisterButtonPressed(message))
            return startRegisterState(chatId);

        if(checkIfLogoutRequest(message))
            return handleLogoutRequest(chatId);

        if (isRegisterRequest(chatId))
            return handleRegisterState(chatId, message);

        if (isLoginRequest(chatId))
            return handleLoginState(chatId,message);

        if(!userIsLoggedIn(chatId))
            return handleStartingChatState(chatId);

        //if(checkIfSendConversationRequest(message))
            //return handleSendConversationRequest(chatId);

        else
            return handleGenerateAnswerState(update);
    }

    private Boolean messageIsNotEmptyOrNull(String message) {
        return (message != null && !message.isEmpty());
    }

    private Boolean checkIfUserLoggedIn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsLoggedIn();
    }

    private LoginUserDTO getLoginUserDTOFromUsersMap(Long chatId) {
        return usersMap.get(chatId);
    }

    private Boolean checkIfLoginButtonPressed(String message) {
        return message.equals("Login");
    }

    private TelegramResponse startLoginState(Long chatId) {

        if(checkIfUserLoggedIn(chatId))
            return handleLoginOrRegisterRequestWhenLoggedIn(chatId);

        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsLoginRequest(true);
        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    private TelegramResponse handleLoginOrRegisterRequestWhenLoggedIn(Long chatId) {
        return getTelegramResponseDTO(chatId,"You are logged in. \n" +
                "Please logout and try again.");
    }

    private Boolean checkIfRegisterButtonPressed(String message) {
        return message.equals("Register");
    }

    private TelegramResponse startRegisterState(Long chatId) {

        if(checkIfUserLoggedIn(chatId))
            return handleLoginOrRegisterRequestWhenLoggedIn(chatId);

        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsRegisterRequest(true);
        return getTelegramResponseDTO(chatId,"Please enter your username.");
    }

    private Boolean checkIfLogoutRequest(String message) {
        return message.equals("Logout");
    }

    private Boolean checkIfSendConversationRequest(String message) {
        return message.equals("Send conversation");
    }

    private void handleSendConversationRequest(Long chatId) {

    }

    private TelegramResponse handleLogoutRequest(Long chatId) {
        startLogoutState(chatId);
        return createTelegramLoginRegisterKeyboardResponse(chatId,
                "Logged out successfully.");
    }

    private void startLogoutState(Long chatId) {
        turnOffLoggedInMode(chatId);
    }

    private void turnOffLoggedInMode(Long chatId) {
        getLoginUserDTOFromUsersMap(chatId).setIsLoggedIn(false);
    }

    private Boolean isLoginRequest(Long chatId) {
        return (getLoginUserDTOFromUsersMap(chatId).getIsLoginRequest());
    }

    private Boolean isRegisterRequest(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsRegisterRequest();
    }

    private Boolean userIsLoggedIn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsLoggedIn();
    }

    private Boolean checkIfUserExists(String username) {
        return authService.checkIfAppUserExists(username);
    }

    private Boolean checkIfUserHasSession(Long chatId) {
        return usersMap.containsKey(chatId);
    }

    private TelegramMessageResponseDTO handleUserInRegistrationState(Long chatId) {
        return getTelegramResponseDTO(chatId,"Please finish registration first.");
    }

    private void createNewSessionForUser(Long chatId) {
        LoginUserDTO loginUserDTO = createLoginUserDTO();

        addUserToUsersMap(loginUserDTO,chatId);

        log.info("New session created for user : {}", loginUserDTO.getUsername());
        log.debug("loginUserDTO : {}",loginUserDTO);
    }

    private TelegramResponse handleStartingChatState(Long chatId)
            throws IOException, InterruptedException {
        createNewSessionForUser(chatId);

        return createTelegramLoginRegisterKeyboardResponse(chatId, "Please login or register.");
    }

    private TelegramResponse createTelegramLoginRegisterKeyboardResponse(Long chatId,
                                                                         String chatMessage) {
        return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                chatId, chatMessage);
    }

    private TelegramResponse handleRegisterState(Long chatId, String message) {
        if(!checkIfUsernameHasBeenSet(chatId))
            return handleUsernameInputForRegistration(chatId,message);

        return handleUserRegistrationWithPasswordInput(chatId,message);
    }

    private TelegramResponse handleLoginState(Long chatId, String message) {
        if(!checkIfUsernameHasBeenSet(chatId))
            return handleUsernameInputForLogin(chatId,message);

        return handleUserLoginWithPasswordInput(chatId,message);
    }

    public TelegramResponse handleGenerateAnswerState(Update update)
            throws IOException, InterruptedException {
        Long chatId = getChatIdFromUpdate(update);
        String username = getUsernameFromLoginUserMap(chatId);
        ChatMessageDTO chatMessageDTO = new ChatMessageDTOMapper().mapToDTO(update, username);
        TelegramMessageResponseDTO telegramResponse = telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);
        String responseMessage = telegramResponse.getText();

        return telegramKeyboardService.createTelegramResponseWithChatMainKeyboard(
                chatMessageDTO.getChatId(),responseMessage);
    }

    private String getUsernameFromLoginUserMap(Long chatId) {
       return getLoginUserDTOFromUsersMap(chatId).getUsername();
    }

    private Long getChatIdFromUpdate(Update update) {
        return update.getMessage().getChatId();
    }

    private Boolean checkIfUsernameHasBeenSet(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getUsername() != null;
    }

    private TelegramMessageResponseDTO handleStartingState(Long chatId) {
        return getTelegramResponseDTO(chatId, "Hi");
    }

    @Transactional
    public TelegramResponse handleUsernameInputForRegistration(Long chatId, String username) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        if (checkIfUserExists(username))
                return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                        chatId, "username is already exists!");
        setUsernameToLoginUserDTO(loginUserDTO, username,chatId);

        log.info("username : {} entered successfully to registration.", username);

        return getTelegramResponseDTO(chatId,
                "username has successfully entered. Please enter password to register.");
    }

    @Transactional
    public TelegramResponse handleUserRegistrationWithPasswordInput(
            Long chatId, String password) {

        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);

        setPasswordToLoginUserDTO(loginUserDTO,password,chatId);

        authService.registerUser(registerDTOMapper.mapToDTO(
                loginUserDTO.getUsername(),
                password));

        log.info("user : {} registered successfully.", loginUserDTO.getUsername());

        turnOffRegistrationState(loginUserDTO,chatId);

        return createTelegramLoginRegisterKeyboardResponse(
                chatId,"You have successfully registered.");
    }

    @Transactional
    public TelegramResponse handleUsernameInputForLogin(Long chatId, String username) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
            if (!checkIfUserExists(username))
                return telegramKeyboardService.createTelegramResponseWithLoginRegisterKeyboard(
                        chatId, "Username does not exists!\n" +
                                "Please try again.");

        setUsernameToLoginUserDTO(loginUserDTO, username,chatId);

        log.info("username : {} entered successfully to login.", username);

        return getTelegramResponseDTO(chatId,
                "Username entered successfully.\n" +
                        "Please enter your password");
    }

    @Transactional
    public TelegramResponse handleUserLoginWithPasswordInput(Long chatId, String password) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);

        setPasswordToLoginUserDTO(loginUserDTO,password,chatId);

        authService.loginUser(loginUserDTO);
        turnOffLogInState(loginUserDTO,chatId);

        log.info("username : {} logged in successfully.", loginUserDTO.getUsername());

        return getTelegramResponseDTO(chatId,"User logged in successfully. " +
                "\nYou are now connected to ChatGPT.");
    }

    private void turnOffRegistrationState(LoginUserDTO loginUserDTO,Long chatId) {
        turnOffUserRegistrationMode(loginUserDTO);
        resetUsernameAndPasswordToUserLoginDTO(loginUserDTO,chatId);
    }

    private void turnOffLogInState(LoginUserDTO loginUserDTO,Long chatId) {
        turnOnUserLoggedInMode(loginUserDTO);
        turnOffUserLoginMode(loginUserDTO);
        resetUsernameAndPasswordToUserLoginDTO(loginUserDTO,chatId);
    }

    private void turnOffUserRegistrationMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsRegisterRequest(false);
    }

    private void resetUsernameAndPasswordToUserLoginDTO(LoginUserDTO loginUserDTO,Long chatId) {
        setPasswordToLoginUserDTO(loginUserDTO,null,chatId);
        setUsernameToLoginUserDTO(loginUserDTO,null,chatId);
    }

    private void turnOnUserLoggedInMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoggedIn(true);
    }

    private void turnOffUserLoginMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoginRequest(false);
    }

    private void setUsernameToLoginUserDTO(LoginUserDTO loginUserDTO, String username,Long chatId) {
        loginUserDTO.setUsername(username);
        addUserToUsersMap(loginUserDTO,chatId);
        log.info("username has been set to user successfully.");
    }

    private void setPasswordToLoginUserDTO(LoginUserDTO loginUserDTO, String password, Long chatId) {
        loginUserDTO.setPassword(password);
        addUserToUsersMap(loginUserDTO,chatId);
        log.info("password has been set to user successfully.");
    }

    private String extractMetadataFromLoginMessage(String message, String subString) {
        return message.substring(subString.length()).trim();
    }

    private void addUserToUsersMap(LoginUserDTO loginUserDTO, Long chatId) {
        usersMap.put(chatId,loginUserDTO);
    }

    private LoginUserDTO createLoginUserDTO() {
        return new LoginUserDTO();
    }

    public TelegramResponse performLogout(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        loginUserDTO.setIsLoggedIn(false);

        return createTelegramLoginRegisterKeyboardResponse(chatId, "user logged out successfully!");
    }

    public TelegramMessageResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }
}
