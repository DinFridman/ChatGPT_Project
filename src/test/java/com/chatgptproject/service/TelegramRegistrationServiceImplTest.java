package com.chatgptproject.service;

import com.chatgptproject.dto.TelegramKeyBoardMessageDTO;
import com.chatgptproject.dto.TelegramMessageResponseDTO;
import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.chatgptproject.security.dto.RegisterDTOMapper;
import com.chatgptproject.security.service.AuthService;
import com.chatgptproject.service.telegramService.TelegramKeyboardServiceImpl;
import com.chatgptproject.model.UserSessionDetails;
import com.chatgptproject.security.dto.RegisterDTO;
import com.chatgptproject.service.telegramService.TelegramRegistrationServiceImpl;
import com.chatgptproject.service.telegramService.TelegramUserStateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import static com.chatgptproject.utils.constants.TelegramResponseConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramRegistrationServiceImplTest {
    @Mock private TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    @Mock private TelegramKeyboardServiceImpl telegramKeyboardServiceImpl;
    @Mock private TelegramResponseDTOMapper telegramResponseDTOMapper;
    @Mock private RegisterDTOMapper registerDTOMapper;
    @Mock private AuthService authService;
    @InjectMocks private TelegramRegistrationServiceImpl underTest;
    private RegisterDTO registerDTO;
    private Long chatId;
    private String message;
    private UserSessionDetails userSessionDetails;

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
        registerDTO = RegisterDTO.builder()
                .username(userSessionDetails.getUsername())
                .password(userSessionDetails.getPassword())
                .build();
    }

    @Test
    void shouldHandleUsernameRegisterInput() {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                        "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(false);
        Mockito.when(authService.checkIfAppUserExists(message))
                .thenReturn(false);
        Mockito.when(telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(chatId,
                        TELEGRAM_USERNAME_ENTERED_SUCCESSFULLY_MESSAGE +
                                "\n" + TELEGRAM_ENTER_PASSWORD_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleRegisterState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnUsernameExistsOnRegisterInput() {
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USERNAME_EXISTS_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(false);
        Mockito.when(authService.checkIfAppUserExists(message))
                .thenReturn(true);
        Mockito.when(telegramKeyboardServiceImpl.createTelegramResponseWithLoginRegisterKeyboard(chatId,
                        TELEGRAM_USERNAME_EXISTS_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleRegisterState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandlePasswordRegisterInput() {
        TelegramKeyBoardMessageDTO expectedTelegramResponseDTO
                = TelegramKeyBoardMessageDTO.builder()
                .chatId(chatId)
                .text(TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE)
                .build();

        Mockito.when(telegramUserStateServiceImpl.isUsernameProvided(chatId))
                .thenReturn(true);
        Mockito.when(telegramUserStateServiceImpl.getUserSessionDetails(chatId))
                .thenReturn(userSessionDetails);
        Mockito.when(registerDTOMapper.mapToDTO(userSessionDetails.getUsername(),
                        userSessionDetails.getPassword()))
                .thenReturn(registerDTO);
        Mockito.when(telegramKeyboardServiceImpl
                        .createTelegramResponseWithLoginRegisterKeyboard(
                                chatId,TELEGRAM_SUCCESSFULLY_REGISTERED_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.handleRegisterState(chatId,message);

        assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

}