package com.jvmdevelop.strife.repo;

import com.jvmdevelop.strife.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByChat_Id(Long chatId, Pageable pageable);
}