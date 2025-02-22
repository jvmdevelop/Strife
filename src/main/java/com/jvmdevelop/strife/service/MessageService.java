package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.model.Message;
import com.jvmdevelop.strife.repo.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepo messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String MESSAGE_CACHE_PREFIX = "message:";

    public Message createMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        redisTemplate.opsForValue().set(MESSAGE_CACHE_PREFIX + savedMessage.getId(), savedMessage, 1, TimeUnit.HOURS);
        return savedMessage;
    }

    public Message findById(Long messageId) {
        Message messageFromCache = (Message) redisTemplate.opsForValue().get(MESSAGE_CACHE_PREFIX + messageId);
        if (messageFromCache == null) {
            Message message = messageRepository.findById(messageId).orElse(null);
            if (message != null) {
                redisTemplate.opsForValue().set(MESSAGE_CACHE_PREFIX + messageId, message, 1, TimeUnit.HOURS);
            }
            return message;
        }
        return messageFromCache;
    }

    public Message updateMessage(Message message) {
        Message updatedMessage = messageRepository.save(message);
        redisTemplate.opsForValue().set(MESSAGE_CACHE_PREFIX + updatedMessage.getId(), updatedMessage, 1, TimeUnit.HOURS);
        return updatedMessage;
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
        redisTemplate.delete(MESSAGE_CACHE_PREFIX + message.getId());
    }

    public List<Message> findMessagesByChatId(Long chatId, int offset) {
        return messageRepository.findByChat_Id(chatId, PageRequest.of(offset, 50));
    }
}