package com.example.chatgptproject.service;

import com.example.chatgptproject.model.AppRoleEntity;
import com.example.chatgptproject.model.ApplicationUser;

import java.util.Optional;

public interface UserService {
    ApplicationUser saveUser(ApplicationUser user);
    void addRoleToUser(String userName, AppRoleEntity role);
    Optional<ApplicationUser> getUser(String userName);
}
