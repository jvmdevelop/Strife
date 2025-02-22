package com.jvmdevelop.strife.repo;

import com.jvmdevelop.strife.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    List<Chat> findByIsTetATetTrueAndUsers_Id(Long userId);
}
