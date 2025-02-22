package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.model.Message;
import com.jvmdevelop.strife.repo.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepo messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public List<Message> findMessagesByChatId(Long chatId, int offset) {
        return messageRepository.findByChat_Id(chatId, PageRequest.of(offset, 50));
    }
}
