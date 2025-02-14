package com.jvmdevelop.strife.repo;

import com.jvmdevelop.strife.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    <T> ScopedValue<T> findByUsername(String username);
}
