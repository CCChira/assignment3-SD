package com.example.chatApp.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserById(Long id);
}
