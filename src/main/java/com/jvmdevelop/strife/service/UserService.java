package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.exception.ExistException;
import com.jvmdevelop.strife.exception.TokenValidException;
import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.repo.UserRepo;
import com.jvmdevelop.strife.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_PREFIX = "user:";

    public User add(User user) throws ExistException {
        try {
            validateEmail(user.getEmail());
            userRepo.save(user);
            redisTemplate.opsForValue().set(USER_CACHE_PREFIX + user.getUsername(), user, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            throw new ExistException("Username or email already exists");
        }
        return user;
    }

    public User getUserInfo(String name) throws RuntimeException {
        User userFromCache = (User) redisTemplate.opsForValue().get(USER_CACHE_PREFIX + name);
        if (userFromCache == null) {
            User user = userRepo.findByUsername(name).orElseThrow(() -> new RuntimeException("No user found with username: " + name));
            redisTemplate.opsForValue().set(USER_CACHE_PREFIX + name, user, 1, TimeUnit.HOURS);
            return user;
        }
        return userFromCache;
    }

    public User getUserByLogin(String username) {
        User userFromCache = (User) redisTemplate.opsForValue().get(USER_CACHE_PREFIX + username);
        if (userFromCache == null) {
            User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("No user found with username: " + username));
            redisTemplate.opsForValue().set(USER_CACHE_PREFIX + username, user, 1, TimeUnit.HOURS);
            return user;
        }
        return userFromCache;
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("No user found with id: " + id));
    }

    public User findById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }

    public List<User> findUsersByIds(List<Long> userIds) {
        return userRepo.findAllById(userIds);
    }

    @Transactional
    public User changeName(String header, String username) {
        String token = getToken(header);
        Optional<User> user = userRepo.findByUsername(token);

        user.get().setUsername(username);
        userRepo.save(user.get());
        redisTemplate.opsForValue().set(USER_CACHE_PREFIX + username, user.get(), 1, TimeUnit.HOURS);

        return user.orElse(null);
    }

    @Transactional
    public User updateAvatar(String avatarUrl, String header) {
        String token = getToken(header);
        User user = userRepo.findByUsername(token)
                .orElseThrow(() -> new RuntimeException("No user found with username: " + JwtUtil.extractUsername(token)));
        user.setAvatarUrl(avatarUrl);
        userRepo.save(user);
        redisTemplate.opsForValue().set(USER_CACHE_PREFIX + user.getUsername(), user, 1, TimeUnit.HOURS);
        return user;
    }

    @Transactional
    public User updateDescription(String description, String header) {
        String token = getToken(header);
        User user = userRepo.findByUsername(token).orElseThrow(() -> new RuntimeException("No user found with username: " + JwtUtil.extractUsername(token)));
        user.setDescription(description);
        userRepo.save(user);
        redisTemplate.opsForValue().set(USER_CACHE_PREFIX + user.getUsername(), user, 1, TimeUnit.HOURS);
        return user;
    }

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("(?i)^[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String token = header.substring(7);
        if (!JwtUtil.validateToken(token)) {
            throw new TokenValidException("Token is not valid");
        }
        return JwtUtil.extractUsername(token);
    }
}