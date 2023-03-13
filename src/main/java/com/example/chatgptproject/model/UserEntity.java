package com.example.chatgptproject.model;


import com.example.chatgptproject.utils.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "UserEntity")
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_phone_unique",
                        columnNames = {"email","phone_number"})//TODO: Add userName,password
        })
public class UserEntity {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long userId;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String phoneNumber;

    @Column(
            name = "chat_id",
            nullable = false
    )
    private Long chatId;

    @Column(
            name = "user_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String userName;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;

    @Column(
            name = "role",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String role;

    public UserEntity(String firstName,
                      String lastName,
                      String email,
                      String phoneNumber,
                      Long chatId,
                      String userName,
                      String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
