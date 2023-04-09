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
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "appUsers")
@Log4j2
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Cacheable(value = "appUsers", key = "#username")
    @Override
    public AppUserEntity getAppUser(String username) {
        Optional<AppUserEntity> user =
                appUserRepository.findAppUserByUsername(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("Username is not found!");
        return user.get();
    }

    @CachePut(value = "appUsers", key = "#appUser.username")
    @Override
    public void addAppUser(AppUserEntity appUser) {
        if(checkIfAppUserExists(appUser.getUsername()))
            throw new UserIsRegisteredException();

        log.info("user saved successfully.");
        log.debug("saved user details: " + appUser);

        appUserRepository.save(appUser);
    }

    public boolean checkIfAppUserExists(String username) {
        return appUserRepository.existsAppUserByUsername(username);
    }
}
