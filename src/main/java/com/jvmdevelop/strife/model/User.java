package com.jvmdevelop.strife.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String role;
    private String avatarUrl;

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats;

    public User(User user ,String username){
        this.username = username;
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.description = user.getDescription();
        this.role = user.getRole();
        this.avatarUrl = user.getAvatarUrl();
    }
}
