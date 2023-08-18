package com.example.chatgptproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity(name = "AppUser")
@NoArgsConstructor
@Table(name = "app_users")
public class AppUserEntity implements Serializable {
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime loggedInDate;


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
