package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findAppUserByUsername(String username);
    boolean existsAppUserByUsername(String username);
}
