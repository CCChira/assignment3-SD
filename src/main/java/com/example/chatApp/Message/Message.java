package com.example.chatApp.Message;

import java.time.LocalDateTime;
import com.example.chatApp.User.User;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;
}
