package com.example.chatApp.Observers;

import com.example.chatApp.EmailService;
import com.example.chatApp.Message.MessageDTO;
import com.example.chatApp.Message.MessageService;
import com.example.chatApp.User.UserDTO;
import com.example.chatApp.User.User;
import com.example.chatApp.User.UserService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class EmailSubscriber implements Observer {
    MessageService messageService;
    EmailService emailService;
    UserService userService;
    public EmailSubscriber(MessageService messageService, EmailService emailService) {
        this.emailService = emailService;
        this.messageService = messageService;
    }

    @Override
    public void update(UserDTO userDTO) {
        System.out.println(userDTO);
        Pair<List<MessageDTO>, HttpStatus> messageDTOS = messageService.getAllMessagesByUser(userDTO);
        StringBuilder sb = new StringBuilder();
        messageDTOS.getFirst().forEach(messageDTO -> {
            sb.append(messageDTO.getSender().getUsername()).append(": ").append(messageDTO.getContent()).append("\n");
        });
        System.out.println(messageDTOS.getFirst());
        emailService.sendEmail(userDTO.getEmail(), userDTO.getUsername(), sb.toString());
    }
}
