package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT m" +
            " FROM ChatMessageEntity m " +
            "WHERE m.chatId = :chatId " +
            "AND m.sentDate >= :startingDate")
    ArrayList<ChatMessageEntity> findMessagesByChatIdAndDate(
            @Param("chatId") Long chatId,
            @Param("startingDate")LocalDate startingDate
    );

    boolean existsChatMessageEntityByChatId(Long chatId);
}
