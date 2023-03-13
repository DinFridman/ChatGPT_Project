package com.example.chatgptproject.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ChatMessageEntity")
@Table(
        name = "chat_messages"
    )
public class ChatMessageEntity {

    @Id
    @SequenceGenerator(
            name = "message_sequence",
            sequenceName = "message_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "message_sequence"
    )
    @Column(
            name = "message_id",
            updatable = false,
            nullable = false
    )
    private Long messageId;

    @Column(
            name = "update_id",
            updatable = false,
            nullable = false
    )
    private Long updateId;

    @Column(
            name = "chat_id",
            nullable = false
    )
    private Long chatId;

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
    private Long userId;

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
