package com.chatgptproject.service;

import com.chatgptproject.model.AppUserEntity;

public interface AppUserService {
    AppUserEntity getAppUserByUsername(String username);
    AppUserEntity getAppUserByUserId(Long userId);
    void addAppUser(AppUserEntity appUser);
    void updateAppUserLoggedInDate(String username);
}
