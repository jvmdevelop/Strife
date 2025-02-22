package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.model.Chat;
import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.repo.ChatRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepo chatRepository;

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    public Chat findTetATetChat(Long currentUserId, Long otherUserId) {
        List<Chat> chats = chatRepository.findByIsTetATetTrueAndUsers_Id(currentUserId);
        for (Chat chat : chats) {
            boolean containsOther = chat.getUsers().stream().anyMatch(u -> u.getId().equals(otherUserId));
            if (containsOther) {
                return chat;
            }
        }
        return null;
    }

    public List<Chat> findChatsByUserId(Long userId) {
        return chatRepository.findByIsTetATetTrueAndUsers_Id(userId);
    }

    public void addUserToChat(Chat chat, User user) {
        chat.getUsers().add(user);
        chatRepository.save(chat);
    }
}
