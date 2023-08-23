package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramMessageResponseDTO;
import com.chatgptproject.dto.mapper.TelegramResponseDTOMapper;
import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.model.UserSessionDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.chatgptproject.utils.constants.TelegramResponseConstants.*;

@ExtendWith(MockitoExtension.class)
class TelegramUserStateServiceImplTest {
    @Mock
    TelegramResponseDTOMapper telegramResponseDTOMapper;
    @Mock TelegramUsersSessionServiceImpl telegramUsersSessionServiceImpl;
    @InjectMocks TelegramUserStateServiceImpl underTest;
    private Long chatId;
    private UserSessionDetails userSessionDetails;

    @BeforeEach
    void setUp() {
        chatId = 1L;
        String message = "test";
        userSessionDetails = UserSessionDetails.builder()
                .chatId(chatId)
                .username("test")
                .password("testPass")
                .build();
    }

    @Test
    void shouldStartRegisterState() {
        userSessionDetails.setIsLoggedIn(false);
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_ENTER_USERNAME_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,TELEGRAM_ENTER_USERNAME_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.startRegisterState(chatId);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnUserLoggedInAlreadyResponseOnRegisterState() {
        userSessionDetails.setIsLoggedIn(true);
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USER_LOGGED_IN_ALREADY)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,TELEGRAM_USER_LOGGED_IN_ALREADY))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.startRegisterState(chatId);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldReturnIsRegisterRequest() {
        boolean expectedResult = true;
        userSessionDetails.setIsRegisterRequest(true);

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        boolean actualResult = underTest.isRegisterRequest(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void shouldReturnIsNotRegisterRequest() {
        boolean expectedResult = false;
        userSessionDetails.setIsRegisterRequest(false);

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        boolean actualResult = underTest.isRegisterRequest(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void shouldReturnUserLoggedIn() {
        boolean expectedResult = true;
        userSessionDetails.setIsLoggedIn(true);

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        boolean actualResult = underTest.isUserLoggedIn(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void shouldReturnUserNotLoggedIn() {
        boolean expectedResult = false;
        userSessionDetails.setIsLoggedIn(false);

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        boolean actualResult = underTest.isUserLoggedIn(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void getUserSessionDetails() {
        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        UserSessionDetails actualResult = underTest.getUserSessionDetails(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(userSessionDetails,actualResult);
    }

    @Test
    void shouldReturnLoggedInAlreadyOnstartLoginState() {
        userSessionDetails.setIsLoggedIn(true);
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_USER_LOGGED_IN_ALREADY)
                .chatId(chatId)
                .build();

        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId,TELEGRAM_USER_LOGGED_IN_ALREADY))
                .thenReturn(expectedTelegramResponseDTO);
        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        TelegramResponse actualResponse = underTest.startLoginState(chatId);

        Assertions.assertEquals(expectedTelegramResponseDTO,actualResponse);
    }

    @Test
    void shouldHandleStartLoginState() {
        userSessionDetails.setIsLoggedIn(false);
        TelegramMessageResponseDTO expectedTelegramResponseDTO
                = TelegramMessageResponseDTO.builder()
                .text(TELEGRAM_ENTER_USERNAME_MESSAGE)
                .chatId(chatId)
                .build();

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);
        Mockito.when(telegramResponseDTOMapper.mapToDTO(chatId, TELEGRAM_ENTER_USERNAME_MESSAGE))
                .thenReturn(expectedTelegramResponseDTO);

        TelegramResponse actualResponse = underTest.startLoginState(chatId);

        Assertions.assertEquals(expectedTelegramResponseDTO, actualResponse);
    }


    @Test
    void shouldReturnUsernameIsProvided() {
        boolean expectedResult = true;
        userSessionDetails.setIsLoggedIn(true);

        Mockito.when(telegramUsersSessionServiceImpl.getUserSessionDetailsFromRepo(chatId))
                .thenReturn(userSessionDetails);

        boolean actualResult = underTest.isUserLoggedIn(chatId);

        Mockito.verify(telegramUsersSessionServiceImpl).getUserSessionDetailsFromRepo(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void setUsernameToUserSession() {
    }

    @Test
    void setPasswordToUserSession() {
    }

    @Test
    void turnOffRegistrationState() {
    }

    @Test
    void turnOffLogInState() {
    }

    @Test
    void handleStartingChatState() {
    }

    @Test
    void checkIfUserHasSession() {
    }

    @Test
    void handleLogoutRequest() {
    }

    @Test
    void checkIfLoginRequest() {
    }

    @Test
    void getUsernameFromUserSessionByChatId() {
    }

    @Test
    void startSendConversationState() {
    }

    @Test
    void checkIfEmailConversationStateOn() {
    }

    @Test
    void turnOnEmailConversationState() {
    }

    @Test
    void turnOffEmailConversationState() {
    }
}