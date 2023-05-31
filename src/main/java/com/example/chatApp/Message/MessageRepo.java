package com.example.chatApp.Message;


import com.example.chatApp.Message.Message;
import com.example.chatApp.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findAllBySender(User sender);
    Message findMessageById(Long id);
}
