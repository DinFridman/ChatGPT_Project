package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT m" +
            " FROM UserEntity m " +
            "WHERE m.userName = :userName")
    UserEntity findUserByUserName(@Param("userName") String userName);

    boolean existsUserEntitiesByUserName(String userName);
}
