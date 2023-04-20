package com.example.chatgptproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "AppUser")
@NoArgsConstructor
@Table(name = "app_users")
public class AppUserEntity {
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

    @JsonIgnore
    @Email
    private String email;

    @Column(
            name = "phone_number",
            updatable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    @JsonIgnore
    private String phoneNumber;

    @Column(
            name = "logged_in_date"
    )
    @Temporal(TemporalType.DATE)
    private LocalDate loggedInDate;


    @Override
    public String toString() {
        return "AppUserEntity{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", loggedInDate=" + loggedInDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserEntity appUser = (AppUserEntity) o;
        return Objects.equals(userId, appUser.userId) && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password) && Objects.equals(email, appUser.email) && Objects.equals(phoneNumber, appUser.phoneNumber) && Objects.equals(loggedInDate, appUser.loggedInDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, phoneNumber, loggedInDate);
    }
}