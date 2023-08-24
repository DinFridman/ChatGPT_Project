package com.example.chatgptproject.service;

import com.example.chatgptproject.exception.register.UserIsRegisteredException;
import com.example.chatgptproject.model.AppUserEntity;
import com.example.chatgptproject.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@CacheConfig(cacheNames = "appUsers")
@Log4j2
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Cacheable(value = "appUsers")
    @Override
    public AppUserEntity getAppUserByUsername(String username) {
        AppUserEntity user =
                appUserRepository.findAppUserEntityByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));

        return user;
    }

    @Cacheable(value = "appUsers")
    @Override
    public AppUserEntity getAppUserByUserId(Long userId) {
        AppUserEntity user =
                appUserRepository.findAppUserEntityByUserId(userId)
                        .orElseThrow(() -> {
                            throw new UsernameNotFoundException("User is not found!");
                        });
        return user;
    }


    @Override
    public void addAppUser(AppUserEntity appUser) {
        if(checkIfAppUserExistsByUsername(appUser.getUsername()))
            throw new UserIsRegisteredException();

        log.info("user saved successfully.");
        log.debug("saved user details: " + appUser);

        appUserRepository.save(appUser);
    }

    @CachePut(value = "appUsers")
    @Override
    public AppUserEntity updateAppUserLoggedInDate(String username) {
        AppUserEntity appUser = getAppUserByUsername(username);
        appUser.setLoggedInDate(LocalDateTime.now());
        appUserRepository.save(appUser);

        log.info("AppUser`s loggedIn date updated successfully. the date is : {}",
                appUser.getLoggedInDate());
        return appUser;
    }

    public boolean checkIfAppUserExistsByUsername(String username) {
        return appUserRepository.existsAppUserEntityByUsername(username);
    }
}
