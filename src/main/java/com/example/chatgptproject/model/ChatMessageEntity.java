package com.example.chatgptproject.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private AppUserEntity user;

    @Column(
            name = "sent_date"
    )
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime sentDate;


    public ChatMessageEntity(Long updateId,
                             Long chatId,
                             String message,
                             String conversationRole,
                             AppUserEntity user,
                             LocalDateTime sentDate) {
        this.updateId = updateId;
        this.chatId = chatId;
        this.message = message;
        this.conversationRole = conversationRole;
        this.user = user;
        this.sentDate = sentDate;
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
