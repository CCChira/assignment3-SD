package com.example.chatApp.Message;

import com.example.chatApp.User.User;
import com.example.chatApp.User.UserDTO;
import com.example.chatApp.User.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    public Message dtoToEntity(MessageDTO messageDTO) {
        return Message.builder()
                .sender(messageDTO.getSender())
                .content(messageDTO.getContent())
                .build();
    }
    public MessageDTO entityToDto(Message message) {
        return MessageDTO.builder()
                .sender(message.getSender())
                .content(message.getContent())
                .build();
    }
    public Pair<String, HttpStatus> createMessage(Message message) {
        System.out.println("HEREEEEEE" + "\n\n\n\n\n\n");
        if (userRepo.findUserByUsername(message.getSender().getUsername()) == null) {
            return Pair.of("User not found", HttpStatus.NOT_FOUND);
        }
        messageRepo.save(message);
        return Pair.of("Message saved successfully", HttpStatus.OK);
    }

    public Pair<List<MessageDTO>, HttpStatus> getAllMessagesByUser(UserDTO userDTO) {
        List<MessageDTO> messageDTOs = new ArrayList<>();

        List<Message> messageList = messageRepo.findAllBySender(
                User.builder()
                        .id(userDTO.getId())
                        .username(userDTO.getUsername())
                        .password(userDTO.getPassword())
                        .loggedIn(userDTO.getLoggedIn())
                        .build());
        messageList.forEach(message ->
          messageDTOs.add(entityToDto(message))
        );

        return Pair.of(messageDTOs, HttpStatus.OK);
    }
}
