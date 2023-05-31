package com.example.chatApp.User;

import com.example.chatApp.EmailService;
import com.example.chatApp.Message.MessageDTO;
import com.example.chatApp.Message.MessageService;
import com.example.chatApp.Observers.ChatSubject;
import com.example.chatApp.Observers.EmailSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepo userRepo;
    private final UserService userService;
    private final MessageService messageService;
    private final EmailService emailService;
    private final ChatSubject chatSubject;
    private final EmailSubscriber emailSubscriber;

    @Autowired
    public UserController(UserRepo userRepo, UserService userService, MessageService messageService, EmailService emailService, ChatSubject chatSubject, EmailSubscriber emailSubscriber) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.messageService = messageService;
        this.emailService = emailService;
        this.chatSubject = chatSubject;
        this.emailSubscriber = emailSubscriber;
        chatSubject.addObserver(emailSubscriber);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO.getUsername() + " " + userDTO.getPassword() + "\n\n\n\n\n");
        Pair<String, HttpStatus> pair = userService.login(userDTO);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        Pair<String, HttpStatus> pair = userService.register(userDTO);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @GetMapping("/getAllOnlineUsers")
    public ResponseEntity<?> getAllOnlineUsers() {
        Pair<?, HttpStatus> pair = userService.getAllOnlineUsers();
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @GetMapping("/sendUserReports")
    public ResponseEntity<?> sendUserReports(@RequestBody UserDTO userDTO) {
        chatSubject.notifyObserver(userDTO);
        return new ResponseEntity<>("done",HttpStatus.OK);
    }
}
