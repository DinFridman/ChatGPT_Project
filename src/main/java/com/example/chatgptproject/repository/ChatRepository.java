package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT m" +
            " FROM ChatMessageEntity m " +
            "WHERE m.user.userId = :userId " +
            "AND m.sentDate >= :startingDate")
    ArrayList<ChatMessageEntity> findMessagesByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("startingDate") LocalDateTime startingDate
    );

}
