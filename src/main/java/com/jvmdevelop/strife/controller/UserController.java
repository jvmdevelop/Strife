package com.jvmdevelop.strife.controller;

import com.jvmdevelop.strife.model.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class UserController {
    @GetMapping("/me")
    public String getCurrentSession(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUsername();
    }
}
