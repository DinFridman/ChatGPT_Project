package com.example.chatgptproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "application_users")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(
            name = "user_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String username;

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
}
