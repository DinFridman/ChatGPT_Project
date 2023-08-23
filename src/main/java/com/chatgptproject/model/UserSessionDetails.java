package com.chatgptproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import java.io.Serializable;
import java.time.LocalDate;

@RedisHash("UserSessionDetails")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserSessionDetails implements Serializable {
    @Id
    private Long chatId;
    private String username;
    private String password;
    private LocalDate loggedInDate;
    private Boolean isLoggedIn = false;
    private Boolean isRegisterRequest = false;
    private Boolean isLoginRequest = false;
    private Boolean isEmailConversationRequest = false;
    @TimeToLive
    private Long expiration = 3600L;
}
