package com.example.chatgptproject.model;


import jakarta.persistence.*;
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
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
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
            name = "app_role",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String appRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_messages",
        joinColumns = @JoinColumn(name = "message_id",
            referencedColumnName = "message_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id")
    )
        private AppUser user;

    public ChatMessageEntity(long updateId,
                             long chatId,
                             String message,
                             long userId,
                             String appRole) {
        this.updateId = updateId;
        this.chatId = chatId;
        this.message = message;
        this.userId = userId;
        this.appRole = appRole;
    }


    @Override
    public String toString() {
        return "ChatMessageEntity{" +
                "updateId=" + updateId +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", appRole='" + appRole + '\'' +
                '}';
    }
}
