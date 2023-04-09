package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity,Long> {
    Optional<AppUserEntity> findAppUserByUsername(String username);
    boolean existsAppUserByUsername(String username);
}
