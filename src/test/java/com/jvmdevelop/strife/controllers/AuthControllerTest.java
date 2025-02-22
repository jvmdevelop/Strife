package com.jvmdevelop.strife.controllers;

import com.jvmdevelop.strife.controller.AuthController;
import com.jvmdevelop.strife.dto.UserDto;
import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.service.UserService;
import com.jvmdevelop.strife.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnJwtToken_whenUserIsRegisteredSuccessfully() {
        UserDto userDto = new UserDto("username", "password", "email", "description", "role", "avatarUrl");
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password("hashedPassword")
                .description(userDto.getDescription())
                .role(userDto.getRole())
                .avatarUrl(userDto.getAvatarUrl())
                .build();

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userService.add(any(User.class))).thenReturn(user);
        when(JwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwtToken");

        String token = authController.register(userDto);

        assertEquals("jwtToken", token);
        verify(userService, times(1)).add(any(User.class));
    }

    @Test
    void login_shouldReturnJwtToken_whenCredentialsAreValid() {
        UserDto userDto = new UserDto("username", "password", "email", "description", "role", "avatarUrl");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(JwtUtil.generateToken(userDetails)).thenReturn("jwtToken");

        String token = authController.login(userDto);

        assertEquals("jwtToken", token);
    }

    @Test
    void register_shouldThrowException_whenUserServiceFails() {
        UserDto userDto = new UserDto("username", "password", "email", "description", "role", "avatarUrl");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");
        doThrow(new RuntimeException("User service failed")).when(userService).add(any(User.class));

        try {
            authController.register(userDto);
        } catch (RuntimeException e) {
            assertEquals("User service failed", e.getMessage());
        }
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {
        UserDto userDto = new UserDto("username", "password", "email", "description", "role", "avatarUrl");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        try {
            authController.login(userDto);
        } catch (RuntimeException e) {
            assertEquals("Authentication failed", e.getMessage());
        }
    }
}