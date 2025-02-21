package com.jvmdevelop.strife.controller;

import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.model.UserDetailsImpl;
import com.jvmdevelop.strife.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public String getCurrentSession(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }

    @GetMapping("/getUserByLogin/{username}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByLogin(username));
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUserInfo(username));
    }

    @PostMapping("/cname")
    public ResponseEntity<User> cname(HttpServletRequest request, @RequestParam String username) {
        String header = request.getHeader("Authorization");
        return ResponseEntity.ok(userService.changeName(header, username));
    }

    @PostMapping("/description")
    public ResponseEntity<User> cdesc(HttpServletRequest request, @RequestParam String description) {
        String header = request.getHeader("Authorization");
        return ResponseEntity.ok(userService.updateDescription(description, header));
    }

    @PostMapping("/avatar")
    public ResponseEntity<User> cavatar(HttpServletRequest request, @RequestParam String avatarUrl) {
        String header = request.getHeader("Authorization");
        return ResponseEntity.ok(userService.updateAvatar(avatarUrl, header));
    }
}
