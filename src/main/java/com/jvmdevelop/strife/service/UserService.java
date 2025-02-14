package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public String getUserInfo(String name) throws Exception{
       User user = (User) userRepo.findByUsername(name).orElseThrow(() -> new RuntimeException("No user found with username: " + name));
       return user.toString();
    }
}
