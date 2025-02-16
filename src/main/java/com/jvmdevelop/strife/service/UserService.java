package com.jvmdevelop.strife.service;

import com.jvmdevelop.strife.model.User;
import com.jvmdevelop.strife.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public String getUserInfo(String name) throws Exception{
       User user = (User) userRepo.findByUsername(name).orElseThrow(() -> new RuntimeException("No user found with username: " + name));
       return user.toString();
    }
    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("(?i)^[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
