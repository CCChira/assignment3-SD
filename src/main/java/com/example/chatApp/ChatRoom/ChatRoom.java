package com.example.chatApp.ChatRoom;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.chatApp.User.User;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_chat_rooms",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    // Other attributes, constructors, and getter/setter methods
}