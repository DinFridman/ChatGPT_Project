package com.example.chatgptproject.service;

import com.example.chatgptproject.model.AppUserEntity;

public interface AppUserService {
    AppUserEntity getAppUserByUsername(String username);
    AppUserEntity getAppUserByUserId(Long userId);
    void addAppUser(AppUserEntity appUser);
}
