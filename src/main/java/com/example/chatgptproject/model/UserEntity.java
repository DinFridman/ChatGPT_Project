package com.example.chatgptproject.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Collection;


@Getter
@Setter
@Entity(name = "UserEntity")
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_name_unique",
                        columnNames = {"user_name"})
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
            name = "user_id",
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<AppRoleEntity> appRoleEntityList = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "user")
    private Collection<ChatMessageEntity> conversation = new ArrayList<>();



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
                ", appRole=" + appRoleEntityList +
                ", conversation=" + conversation +
                '}';
    }
}
