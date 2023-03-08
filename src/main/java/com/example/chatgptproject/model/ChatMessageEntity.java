package com.example.chatgptproject.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ChatMessageEntity")
@Table(
        name = "chat_messages",
        uniqueConstraints = {
                @UniqueConstraint(name = "chat_update_id_unique",
                        columnNames = {"update_id"})//TODO: Add chatId,userId
        })
public class ChatMessageEntity {
    @Id
    @Column(
            name = "update_id",
            updatable = false
    )
    private long updateId;

    @Column(
            name = "chat_id",
            nullable = false
    )
    private long chatId;

    @Column(
            name = "message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(
            name = "user_id",
            nullable = false
    )
    private long userId;

    @Column(
            name = "role",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String role;

    public ChatMessageEntity(long updateId,
                             long chatId,
                             String message,
                             long userId,
                             String role) {
        this.updateId = updateId;
        this.chatId = chatId;
        this.message = message;
        this.userId = userId;
        this.role = role;
    }


    @Override
    public String toString() {
        return "ChatMessageEntity{" +
                "updateId=" + updateId +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                '}';
    }
}
