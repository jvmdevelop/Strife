package com.jvmdevelop.strife.controller;

import com.jvmdevelop.strife.model.UserDetailsImpl;
import com.jvmdevelop.strife.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public String getCurrentSession(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }

    @GetMapping("/getUserInfo")
    public String getUserInfo(@PathVariable String username) throws Exception {
        return userService.getUserInfo(username);
    }
}
