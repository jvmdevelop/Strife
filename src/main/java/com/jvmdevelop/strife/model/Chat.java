package com.jvmdevelop.strife.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean isTetATet;
    @ManyToOne
    private User recepient;
    private Long recepientId;
    @OneToMany
    private List<Messages> messages;
    @OneToMany
    private List<User> users;
}
