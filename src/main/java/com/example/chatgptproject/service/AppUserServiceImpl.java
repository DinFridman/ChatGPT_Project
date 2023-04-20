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

import java.time.LocalDate;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "appUsers")
@Log4j2
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Cacheable(value = "appUsers", key = "#username")
    @Override
    public AppUserEntity getAppUserByUsername(String username) {
        Optional<AppUserEntity> user =
                appUserRepository.findAppUserEntityByUsername(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("Username is not found!");
        return user.get();
    }

    @Cacheable(value = "appUsers", key = "#userId")//TODO: could make problems
    @Override
    public AppUserEntity getAppUserByUserId(Long userId) {
        Optional<AppUserEntity> user =
                appUserRepository.findAppUserEntityByUserId(userId);
        if(user.isEmpty())
            throw new UsernameNotFoundException("Username is not found!");
        return user.get();
    }


    @Override
    public void addAppUser(AppUserEntity appUser) {
        if(checkIfAppUserExists(appUser.getUsername()))
            throw new UserIsRegisteredException();

        log.info("user saved successfully.");
        log.debug("saved user details: " + appUser);

        appUserRepository.save(appUser);
    }

    @CachePut(value = "appUsers", key = "#username")
    public void updateAppUserLoggedInDate(String username) {
        AppUserEntity appUser = getAppUserByUsername(username);
        appUser.setLoggedInDate(LocalDate.now());
        appUserRepository.save(appUser);

        log.info("AppUser`s loggedIn date updated successfully. the date is : {}",
                appUser.getLoggedInDate());
    }

    public boolean checkIfAppUserExists(String username) {
        return appUserRepository.existsAppUserEntityByUsername(username);
    }
}
