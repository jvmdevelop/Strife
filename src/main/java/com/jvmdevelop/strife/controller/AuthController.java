package com.jvmdevelop.strife.controller;

import com.jvmdevelop.strife.dto.UserDto;
import com.jvmdevelop.strife.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody UserDto user) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
                );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return JwtUtil.generateToken(userDetails);
    }

    @GetMapping("/hash")
    public String hashPassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}
