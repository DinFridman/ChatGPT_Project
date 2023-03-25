package com.example.chatgptproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "AppUser")
@NoArgsConstructor
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "user_id",
            updatable = false
    )
    private Long userId;

    @Column(
            name = "username",
            updatable = false,
            unique = true,
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String username;

    @Column(
            name = "password",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"
    )
    @JsonIgnore
    private String password;

    @Column(
            name = "email",
            updatable = false,
            columnDefinition = "TEXT",
            unique = true
    )
    @JsonIgnore
    private String email;

    @Column(
            name = "phone_number",
            updatable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    @JsonIgnore
    private String phoneNumber;

}
