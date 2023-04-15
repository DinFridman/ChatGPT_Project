package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.dto.LoginUserDTO;
import com.example.chatgptproject.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public Boolean checkIfUserLoggedIn(Long chatId) {
        return getLoginUserDTOFromUsersMap(chatId).getIsLoggedIn();
    }

    public LoginUserDTO getLoginUserDTOFromUsersMap(Long chatId) {
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
        loginUserDTO.setIsRegisterRequest(true);
        addUserToUsersMap(loginUserDTO,chatId);

        log.info("user : {} is on register request mode.", loginUserDTO.getUsername());
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
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        turnOffUserRegistrationMode(loginUserDTO);
        resetUsernameAndPasswordToUserLoginDTO(chatId);
    }

    private void turnOffUserRegistrationMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsRegisterRequest(false);
    }

    private void resetUsernameAndPasswordToUserLoginDTO(Long chatId) {
        setPasswordToLoginUserDTO(null,chatId);
        setUsernameToLoginUserDTO(null,chatId);
    }

    public void turnOffLogInState(Long chatId) {
        LoginUserDTO loginUserDTO = getLoginUserDTOFromUsersMap(chatId);
        turnOnUserLoggedInMode(loginUserDTO);
        turnOffUserLoginMode(loginUserDTO);
        resetUsernameAndPasswordToUserLoginDTO(chatId);
    }

    private void turnOnUserLoggedInMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoggedIn(true);
    }

    private void turnOffUserLoginMode(LoginUserDTO loginUserDTO) {
        loginUserDTO.setIsLoginRequest(false);
    }



}
