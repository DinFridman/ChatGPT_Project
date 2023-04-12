package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTO;
import com.example.chatgptproject.security.dto.LoginUserDTOMapper;
import com.example.chatgptproject.security.dto.RegisterDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramGatewayService {
    private final AuthService authService;
    private final RegisterDTOMapper registerDTOMapper;
    private final LoginUserDTOMapper loginUserDTOMapper;
    private final TelegramResponseDTOMapper telegramResponseDTOMapper;
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final Map<Long,LoginUserDTO> usersMap = new HashMap<>();

    /*public TelegramResponseDTO telegramRequestsGateway(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        String message = chatMessageDTO.getMessage();
        Long chatId = chatMessageDTO.getChatId();

        if (message != null && !message.isEmpty()) {
            if (message.startsWith("/start")) {
                return handleStartingState(chatId);
            } else if (message.startsWith("/register")) {
                return handleRegisterState(chatId, message);
            } else if (message.startsWith("/login")) {
                return handleLoginState(chatId,message);
            } else {
                return handleGenerateAnswerState(chatMessageDTO);
            }
        }
        return getTelegramResponseDTO(chatId, "no message!");
    }*/


    public TelegramResponseDTO handleGenerateAnswerState(ChatMessageDTO chatMessageDTO)
            throws IOException, InterruptedException {
        return telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTO);
    }

        public TelegramResponseDTO handleStartingState(Long chatId) {
        return getTelegramResponseDTO(chatId,
                "Please enter your password to register or login."
                        + " send /register or /login and then your password.");
    }

    @Transactional
    public TelegramResponseDTO createUserRegistrationSession(Message message) {
        Long chatId = message.getChatId();
        String username = extractMetadataFromLoginMessage(message.getText(),
                "/register/createSession");

        if(checkIfUserIsRegistered(chatId))
            return getTelegramResponseDTO(chatId, "user is registered already!");

        LoginUserDTO loginUserDTO = createLoginUserDTO(username);

        usersMap.put(chatId,loginUserDTO);

        return getTelegramResponseDTO(message.getChatId(),
                "username entered. Please enter password to register.");
    }

    @Transactional
    public TelegramResponseDTO performUserRegistration(Message message) {
        String password = extractMetadataFromLoginMessage(message.getText(),
                "/register/performRegistration");
        Long chatId = message.getChatId();
        LoginUserDTO loginUserDTO = usersMap.get(chatId);

        addPasswordToUser(loginUserDTO,password);

        authService.registerUser(registerDTOMapper.mapToDTO(
                loginUserDTO.getUsername(),
                password));

        handleRegistrationState(loginUserDTO);

        return getTelegramResponseDTO(message.getChatId(),
                "user registered successfully.");
    }

    @Transactional
    public TelegramResponseDTO createUserLoginSession(Message message) {
        String username = extractMetadataFromLoginMessage(message.getText(),
                "/login/createSession");
        Long chatId = message.getChatId();

        LoginUserDTO loginUserDTO = createLoginUserDTO(username);

        log.debug("loginUserDTO : {}",loginUserDTO);

        addUserToUsersMap(loginUserDTO,chatId);

        log.info("new user added to users map successfully.");

        return getTelegramResponseDTO(message.getChatId(),
                "username entered. Please enter your password");
    }

    @Transactional
    public TelegramResponseDTO performUserLogin(Message message) {
        String password = extractMetadataFromLoginMessage(message.getText(),
                "/login/performLogin");
        Long chatId = message.getChatId();
        LoginUserDTO loginUserDTO = usersMap.get(chatId);

        addPasswordToUser(loginUserDTO,password);

        authService.loginUser(loginUserDTO);
        handleLogInState(loginUserDTO);

        return getTelegramResponseDTO(message.getChatId(),
                "user logged in successfully.");
    }

    private void handleRegistrationState(LoginUserDTO loginUserDTO) {
        turnUserRegistrationModeOff(loginUserDTO);
    }

    private void handleLogInState(LoginUserDTO loginUserDTO) {
        turnUserLoggedInModeOn(loginUserDTO);
    }

    private void turnUserRegistrationModeOff(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsRegisterRequest(false);
    }

    private void turnUserLoggedInModeOn(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoggedIn(true);
    }

    private void addPasswordToUser(LoginUserDTO loginUserDTO, String password) {
        loginUserDTO.setPassword(password);
        log.info("password added successfully to user.");
    }

    private Boolean checkIfUserIsRegistered(Long chatId) {
        return usersMap.containsKey(chatId) && !usersMap.get(chatId).getIsRegisterRequest();
    }

    private String extractMetadataFromLoginMessage(String message, String subString) {
        return message.substring(subString.length()).trim();
    }

    private void addUserToUsersMap(LoginUserDTO loginUserDTO, Long chatId) {
        usersMap.put(chatId,loginUserDTO);
    }

    private LoginUserDTO createLoginUserDTO(String username) {
        return loginUserDTOMapper.mapToDTO(username);
    }

    public TelegramResponseDTO performLogout(Long chatId) {
        LoginUserDTO loginUserDTO = usersMap.get(chatId);
        loginUserDTO.setIsLoggedIn(false);

        return getTelegramResponseDTO(chatId,"user logged out successfully!");
    }

    public TelegramResponseDTO getTelegramResponseDTO(Long chatId, String message) {
        return telegramResponseDTOMapper.mapToDTO(chatId,message);
    }
}
