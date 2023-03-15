package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.AppRoleEntity;
import com.example.chatgptproject.utils.enums.AppRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRolesRepository extends JpaRepository<AppRoleEntity,Long> {
    Optional<AppRoleEntity> findByName(AppRoles name);
}
