package com.chatgptproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserDTO {
    private String username;
    private String password;
}
