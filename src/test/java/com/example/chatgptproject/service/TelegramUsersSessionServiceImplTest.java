package com.example.chatgptproject.service;

import com.example.chatgptproject.model.UserSessionDetails;
import com.example.chatgptproject.repository.UserSessionDetailsRepository;
import com.example.chatgptproject.service.telegramService.TelegramUsersSessionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TelegramUsersSessionServiceImplTest {
    @Mock private UserSessionDetailsRepository userSessionDetailsRepository;
    @InjectMocks TelegramUsersSessionServiceImpl underTest;
    private UserSessionDetails userSessionDetails;
    private Long chatId;

    @BeforeEach
    void setUp() {
        chatId = 1L;
        userSessionDetails = UserSessionDetails.builder()
                .chatId(chatId)
                .username("test")
                .password("testPass")
                .build();
    }

    @Test
    void shouldCreateNewSessionForUser() {
        underTest.createNewSessionForUser(userSessionDetails);

        Mockito.verify(userSessionDetailsRepository).save(userSessionDetails);
    }

    @Test
    void shouldGetUserSessionDetailsFromRepo() {
        Mockito.when(userSessionDetailsRepository.findById(chatId))
                .thenReturn(Optional.of(userSessionDetails));

        underTest.getUserSessionDetailsFromRepo(chatId);

        Mockito.verify(userSessionDetailsRepository).findById(chatId);
    }

    @Test
    void shouldThrowUserSessionDetailsNotFoundFromRepo() {
        Mockito.when(userSessionDetailsRepository.findById(chatId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> underTest.getUserSessionDetailsFromRepo(chatId));

        Mockito.verify(userSessionDetailsRepository).findById(chatId);
    }

    @Test
    void shouldAddUserSessionDetails() {
        underTest.addUserSessionDetails(userSessionDetails);

        Mockito.verify(userSessionDetailsRepository).save(userSessionDetails);
    }

    @Test
    void shouldUpdateUserSessionDetails() {
        Mockito.when(userSessionDetailsRepository.save(userSessionDetails))
                .thenReturn(userSessionDetails);

        underTest.updateUserSessionDetails(userSessionDetails);

        Mockito.verify(userSessionDetailsRepository).save(userSessionDetails);
    }

    @Test
    void shouldRemoveUserSessionDetails() {

        underTest.removeUserSessionDetails(chatId);

        Mockito.verify(userSessionDetailsRepository).deleteById(chatId);
    }

    @Test
    void shouldReturnUserSessionExistsByChatId() {
        boolean expectedResult = true;
        Mockito.when(userSessionDetailsRepository.existsById(chatId))
                .thenReturn(expectedResult);

        boolean actualResult = underTest.checkIfUserSessionExistsByChatId(chatId);

        Mockito.verify(userSessionDetailsRepository).existsById(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

    @Test
    void shouldReturnUserSessionNotExistsByChatId() {
        boolean expectedResult = false;
        Mockito.when(userSessionDetailsRepository.existsById(chatId))
                .thenReturn(expectedResult);

        boolean actualResult = underTest.checkIfUserSessionExistsByChatId(chatId);

        Mockito.verify(userSessionDetailsRepository).existsById(chatId);
        Assertions.assertEquals(expectedResult,actualResult);
    }

}