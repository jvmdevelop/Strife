package com.jvmdevelop.strife.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Long senderId;
    private Long chatId;
    @ManyToOne
    private User sender;

    @ManyToMany
    private Chat chat;


}
