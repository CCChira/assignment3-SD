package com.example.chatApp.Utils;

import com.example.chatApp.Message.ChatMessage;
import com.example.chatApp.Message.Message;
import com.example.chatApp.Message.MessageService;
import com.example.chatApp.User.User;
import com.example.chatApp.User.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final UserService userService;
    private final MessageService messageService;

    public ChatController(UserService userService, MessageService messageService){
        this.userService = userService;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage.getSender() + " " + chatMessage.getContent() + "\n\n\n\n\n");
        if(!userService.isLoggedIn(chatMessage.getSender())) {
            chatMessage.setContent("User not logged in");
        }
        Message message = Message.builder()
                    .sender(userService.getUser(chatMessage.getSender()))
                    .content(chatMessage.getContent())
                    .build();
        messageService.createMessage(message);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(chatMessage.getSender() + " " + chatMessage.getSenderPass() + "\n\n\n\n\n");
        if(userService.isLoggedIn(chatMessage.getSender())){
            chatMessage.setContent("User already logged in");
            return chatMessage;
        }
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}