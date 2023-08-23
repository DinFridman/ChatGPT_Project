package com.chatgptproject.service;

import com.chatgptproject.exception.register.UserIsRegisteredException;
import com.chatgptproject.model.AppUserEntity;
import com.chatgptproject.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {
    @Mock private AppUserRepository appUserRepository;
    @InjectMocks private AppUserServiceImpl underTest;
    private AppUserEntity appUser;


    @BeforeEach
    public void setup() {
        appUser = new AppUserEntity();
        appUser.setUserId(1L);
        appUser.setUsername("Test");
        appUser.setPassword("Testpass");
        appUser.setLoggedInDate(LocalDateTime.now());

    }

    @Test
    void shouldGetAppUserByUsername() {
        Mockito.when(appUserRepository.findAppUserEntityByUsername(appUser.getUsername())).
                thenReturn(Optional.of(appUser));

        AppUserEntity actualAppUser = underTest.getAppUserByUsername(appUser.getUsername());

        verify(appUserRepository).findAppUserEntityByUsername(appUser.getUsername());
        assertEquals(appUser, actualAppUser);
    }

    @Test
    void shouldNotGetAppUserByUsername() {
        Mockito.when(appUserRepository.findAppUserEntityByUsername("wrongUsername"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class
                , () -> underTest.getAppUserByUsername("wrongUsername"));

        verify(appUserRepository).findAppUserEntityByUsername("wrongUsername");
    }

    @Test
    void shouldGetAppUserByUserId() {
        Mockito.when(appUserRepository.findAppUserEntityByUserId(appUser.getUserId())).
                thenReturn(Optional.of(appUser));

        AppUserEntity actualAppUser = underTest.getAppUserByUserId(appUser.getUserId());

        verify(appUserRepository).findAppUserEntityByUserId(appUser.getUserId());
        assertEquals(appUser, actualAppUser);
    }

    @Test
    void shouldNotGetAppUserByUserId() {
        Long wrongUserId = 10L;
        Mockito.when(appUserRepository.findAppUserEntityByUserId(10L))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> underTest.getAppUserByUserId(wrongUserId));

        verify(appUserRepository).findAppUserEntityByUserId(wrongUserId);
    }

    @Test
    void shouldAddAppUser() {
        Mockito.when(appUserRepository.existsAppUserEntityByUsername(appUser.getUsername()))
                .thenReturn(false);

        underTest.addAppUser(appUser);

        verify(appUserRepository).save(appUser);
    }

    @Test
    void shouldNotAddAppUser() {
        Mockito.when(appUserRepository.existsAppUserEntityByUsername(appUser.getUsername()))
                .thenReturn(true);

        assertThrows(UserIsRegisteredException.class
                , () -> underTest.addAppUser(appUser));
    }

    @Test
    void shouldUpdateAppUserLoggedInDate() {
        String username = appUser.getUsername();

        Mockito.when(appUserRepository.findAppUserEntityByUsername(username)).
                thenReturn(Optional.of(appUser));
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);

        underTest.updateAppUserLoggedInDate(username);

        verify(appUserRepository).save(appUser);
    }

    @Test
    void shouldReturnAppUserExistsByUsername() {
        String username = appUser.getUsername();
        Mockito.when(appUserRepository.existsAppUserEntityByUsername(username))
                .thenReturn(true);

        boolean result = underTest.checkIfAppUserExistsByUsername(username);

        verify(appUserRepository).existsAppUserEntityByUsername(username);
        assertTrue(result);
    }

    @Test
    void shouldReturnAppUserNotExistsByUsername() {
        String username = appUser.getUsername();
        Mockito.when(appUserRepository.existsAppUserEntityByUsername(username))
                .thenReturn(false);

        boolean result = underTest.checkIfAppUserExistsByUsername(username);

        verify(appUserRepository).existsAppUserEntityByUsername(username);
        assertFalse(result);
    }

}