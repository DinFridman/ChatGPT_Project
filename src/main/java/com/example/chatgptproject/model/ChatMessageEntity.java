package com.example.chatgptproject.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ChatMessageEntity")
@Table(name = "chat_messages")
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
            name = "app_role",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String conversationRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private AppUser user;

    public ChatMessageEntity(Long updateId,
                             Long chatId,
                             String message,
                             String conversationRole,
                             AppUser user) {
        this.updateId = updateId;
        this.chatId = chatId;
        this.message = message;
        this.conversationRole = conversationRole;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ChatMessageEntity{" +
                "messageId=" + messageId +
                ", updateId=" + updateId +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", conversationRole='" + conversationRole + '\'' +
                ", user=" + user +
                '}';
    }
}
