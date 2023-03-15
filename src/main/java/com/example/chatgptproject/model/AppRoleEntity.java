package com.example.chatgptproject.model;

import com.example.chatgptproject.utils.enums.AppRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_roles")
public class AppRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AppRoles name;
}
