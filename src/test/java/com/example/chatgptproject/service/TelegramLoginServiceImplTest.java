package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.*;
import com.example.chatgptproject.dto.mapper.LoginUserDTOMapper;
import com.example.chatgptproject.model.UserSessionDetails;
import com.example.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import com.example.chatgptproject.service.telegramService.TelegramKeyboardServiceImpl;
import com.example.chatgptproject.service.telegramService.TelegramLoginServiceImpl;
import com.example.chatgptproject.service.telegramService.TelegramUserStateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;

import static com.example.chatgptproject.utils.constants.TelegramResponseConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramLoginServiceImplTest {
    @Mock private TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    @Mock private AuthService authService;
    @Mock private TelegramResponseDTOMapper telegramResponseDTOMapper;
    @Mock private TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    @Mock private LoginUserDTOMapper loginUserDTOMapper;
    @InjectMocks private TelegramLoginServiceImpl underTest;
    private Long chatId;
    private String message;
    private UserSessionDetails userSessionDetails;
    private LoginUserDTO loginUserDTO;

    @BeforeEach
    void setUp() {
        chatId = 1L;
        message = "testMessage";
        userSessionDetails = UserSessionDetails.builder()
                .chatId(chatId)
                .username("test")
                .password("testPass")
                .loggedInDate(LocalDateTime.MAX.toLocalDate())
                .build();
        loginUserDTO = LoginUserDTO.builder()
                .username(userSessionDetails.getUsername())
                .password(userSessionDetails.getPassword())
                .build();
    }

    @Test
    void shouldHandleUsernameLoginInput() {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(false);
        Mockito.when(authService.checkIfAppUserExists(message))
                .thenReturn(true);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,
                TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleLoginState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnUsernameDoesntExistsOnUsernameLoginInput() {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                        .thenReturn(false);
        Mockito.when(authService.checkIfAppUserExists(message))
                .thenReturn(false);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,
                        TELEGRAM_USERNAME_DOESNT_EXISTS_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleLoginState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandlePasswordLoginInput() {
        TelegramKeyBoardMessageDTO expectedTelegramResponseDTO
                = TelegramKeyBoardMessageDTO.builder()
                .chatId(chatId)
                .text(TELEGRAM_USER_LOGGED_IN_SUCCESSFULLY_RESPONSE)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(true);
        Mockito.when(telegramUserStateServiceImpl.getUserSessionDetails(chatId))
                        .thenReturn(userSessionDetails);
        Mockito.when(loginUserDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                        userSessionDetails.getPassword()))
                        .thenReturn(loginUserDTO);
        Mockito.when(telegramKeyboardServiceImpl
                        .createTelegramResponseWithChatMainKeyboard(
                                chatId,TELEGRAM_USER_LOGGED_IN_SUCCESSFULLY_RESPONSE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleLoginState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnInvalidPasswordLoginInput() {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_INCORRECT_PASSWORD_ENTERED)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(true);
        Mockito.when(telegramUserStateServiceImpl.getUserSessionDetails(chatId))
                .thenReturn(userSessionDetails);
        Mockito.when(loginUserDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                        userSessionDetails.getPassword()))
                .thenReturn(loginUserDTO);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,
                        TELEGRAM_INCORRECT_PASSWORD_ENTERED))
                .thenReturn(expectedTelegramResponseDTO);
        Mockito.when(authService.loginUser(loginUserDTO)).thenThrow(BadCredentialsException.class);

        TelegramResponse actualResponse = underTest.handleLoginState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }
}