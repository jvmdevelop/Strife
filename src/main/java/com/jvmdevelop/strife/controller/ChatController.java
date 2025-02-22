package com.jvmdevelop.strife.controller;

import com.jvmdevelop.strife.model.*;
import com.jvmdevelop.strife.reqandresp.*;
import com.jvmdevelop.strife.service.ChatService;
import com.jvmdevelop.strife.service.MessageService;
import com.jvmdevelop.strife.service.UserService;
import com.jvmdevelop.strife.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    private UserService userService;

    private MessageService messageService;

    private JwtUtil jwtUtil;

    @PostMapping("/createChat")
    public ResponseEntity<?> createChat(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody CreateChatRequest request) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long currentUserId;
        try {
            currentUserId = jwtUtil.validateAndGetUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("At least one user must be specified");
        }
        if ((request.getIsTetATet() != null && request.getRecipientId() == null) ||
                (request.getIsTetATet() == null && request.getRecipientId() != null)) {
            return ResponseEntity.badRequest().body("Both is_tet_a_tet and recipient_id must be provided together");
        }
        request.getUserIds().add(currentUserId);
        List<User> users = userService.findUsersByIds(request.getUserIds());
        if (users == null || users.isEmpty()) {
            return ResponseEntity.badRequest().body("Some users not found");
        }

        Chat chat = new Chat();
        chat.setTitle(request.getTitle());
        chat.setUsers(users);
        chat.setIsTetATet(request.getIsTetATet() != null ? request.getIsTetATet() : false);
        chat.setRecipientId(request.getRecipientId() != null ? request.getRecipientId() : 1L);

        Chat createdChat = chatService.createChat(chat);
        return ResponseEntity.ok(createdChat);
    }

    @PostMapping("/getCurrentChat")
    public ResponseEntity<?> getCurrentChat(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody GetCurrentChatRequest request) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long currentUserId;
        try {
            currentUserId = jwtUtil.validateAndGetUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Chat targetChat = chatService.findTetATetChat(currentUserId, request.getUserId());
        if (targetChat == null) {
            List<User> users = userService.findUsersByIds(List.of(currentUserId, request.getUserId()));
            if (users.size() < 2) {
                return ResponseEntity.badRequest().body("Some users not found");
            }
            Chat newChat = new Chat();
            newChat.setTitle("Tet-a-tet chat");
            newChat.setUsers(users);
            newChat.setIsTetATet(true);
            newChat.setRecipientId(request.getUserId());
            targetChat = chatService.createChat(newChat);
        }
        return ResponseEntity.ok(targetChat);
    }

    @PostMapping("/addUserToChat")
    public ResponseEntity<?> addUserToChat(@RequestBody AddUserToChatRequest request) {
        Chat chat = chatService.findById(request.getChatId());
        if (chat == null) {
            return ResponseEntity.status(404).body("Chat not found");
        }
        User user = userService.findById(request.getUserId());
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        chatService.addUserToChat(chat, user);
        return ResponseEntity.ok("User added to chat successfully");
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody SendMessageRequest request) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long senderId;
        try {
            senderId = jwtUtil.validateAndGetUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        Chat chat = chatService.findById(request.getChatId());
        if (chat == null) {
            return ResponseEntity.status(404).body("Chat not found");
        }
        User sender = userService.findById(senderId);
        if (sender == null) {
            return ResponseEntity.status(404).body("Sender not found");
        }
        Message message = new Message();
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setSender(sender);
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    @PostMapping("/getChatMessages")
    public ResponseEntity<?> getChatMessages(@RequestBody GetChatMessagesRequest request) {
        Chat chat = chatService.findById(request.getChatId());
        if (chat == null) {
            return ResponseEntity.status(404).body("Chat not found");
        }
        List<Message> messages = messageService.findMessagesByChatId(request.getChatId(), request.getOffset());
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/editMessage")
    public ResponseEntity<?> editMessage(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody EditMessageRequest request) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId;
        try {
            userId = jwtUtil.validateAndGetUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        Message message = messageService.findById(request.getMessageId());
        if (message == null) {
            return ResponseEntity.status(404).body("Message not found");
        }
        if (!message.getSender().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only edit your own messages");
        }
        message.setContent(request.getContent());
        Message updatedMessage = messageService.updateMessage(message);
        return ResponseEntity.ok(updatedMessage);
    }

    @PostMapping("/deleteMessage")
    public ResponseEntity<?> deleteMessage(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody DeleteMessageRequest request) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId;
        try {
            userId = jwtUtil.validateAndGetUserId(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        Message message = messageService.findById(request.getMessageId());
        if (message == null) {
            return ResponseEntity.status(404).body("Message not found");
        }
        if (!message.getSender().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only delete your own messages");
        }
        messageService.deleteMessage(message);
        return ResponseEntity.ok("Message deleted successfully");
    }
}