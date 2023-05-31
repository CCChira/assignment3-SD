package com.example.chatApp.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name="ChatUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    @Value("false")
    private Boolean loggedIn;


    public User(Long id, String email, String username, String password, Boolean loggedIn) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
        this.loggedIn = false;
    }
}
