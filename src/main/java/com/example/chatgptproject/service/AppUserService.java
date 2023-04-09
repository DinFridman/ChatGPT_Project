package com.example.chatgptproject.service;

import com.example.chatgptproject.model.AppUserEntity;

public interface AppUserService {
    AppUserEntity getAppUser(String username);
    void addAppUser(AppUserEntity appUser);
}
