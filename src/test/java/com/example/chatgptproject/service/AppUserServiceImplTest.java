package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.ConversationDTO;
import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.mapper.OpenAIPromptDTOMapper;
import com.example.chatgptproject.dto.openAI.OpenAIPromptDTO;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.repository.AppUserRepository;
import com.example.chatgptproject.utils.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {
    @Mock private AppUserRepository appUserRepository;
    private AppUserEntity appUser;

    @BeforeEach
    public void setup() {
        appUser = new AppUserEntity();
        appUser.setUserId(1L);
        appUser.setUsername("Test");
        appUser.setPassword("TestPass");
        appUser.setLoggedInDate(LocalDateTime.now());
    }

    @Test
    void getAppUserByUsername() {
    }

    @Test
    void getAppUserByUserId() {
    }

    @Test
    void addAppUser() {
    }

    @Test
    void updateAppUserLoggedInDate() {
    }

    @Test
    void checkIfAppUserExists() {
    }
}