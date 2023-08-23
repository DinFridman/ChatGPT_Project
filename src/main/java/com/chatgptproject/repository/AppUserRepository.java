package com.chatgptproject.repository;

import com.chatgptproject.model.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity,Long> {
    Optional<AppUserEntity> findAppUserEntityByUserId(Long userId);
    boolean existsAppUserEntityByUserId(Long userId);
    Optional<AppUserEntity> findAppUserEntityByUsername(String username);
    boolean existsAppUserEntityByUsername(String username);
}
