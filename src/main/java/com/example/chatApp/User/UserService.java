package com.example.chatApp.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    public Boolean passwordCheck(String pass, String passEncoded) {
        System.out.println(pass + " " + Base64.getEncoder().encodeToString(pass.getBytes()) + "  " + passEncoded + "\n\n\n\n\n");
        return Base64.getEncoder().encodeToString(pass.getBytes()).equals(passEncoded);
    }
    public User findByUserName(String username) {
        return userRepo.findUserByUsername(username);
    }
    public Boolean isLoggedIn(String username) {
        User userFound = findByUserName(username);
        if(userFound == null) return false;
        return userFound.getLoggedIn();
    }
    public User getUser(String username) {
        return userRepo.findUserByUsername(username);
    }
    public Pair<String, HttpStatus> notLoggedIn(String username) {
        return Pair.of("You are not logged in", HttpStatus.UNAUTHORIZED);
    }
    public Pair<String, HttpStatus> login(UserDTO user) {
        System.out.println("Im in login" + user.getUsername() + " " + user.getPassword() + "\n\n\n\n\n");
        User userFound = findByUserName(user.getUsername());
        if (!passwordCheck(user.getPassword(), userFound.getPassword())){
            System.out.println("Passwords: " + user.getPassword() + " " + userFound.getPassword() + "\n\n\n\n\n");
            return Pair.of("Wrong password", HttpStatus.UNAUTHORIZED);
        }
        userFound.setLoggedIn(true);
        userRepo.save(userFound);
        return Pair.of("Successful user login", HttpStatus.OK);
    }
    public User dtoToEntity(UserDTO userDTO) {
        return User.builder().username(userDTO.getUsername())
                             .password(Base64.getEncoder().encodeToString(userDTO.getPassword().getBytes()))
                             .loggedIn(userDTO.getLoggedIn())
                             .email(userDTO.getEmail())
                             .id(userDTO.getId())
                             .build();
    }
    public UserDTO entityToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .loggedIn(user.getLoggedIn())
                .id(user.getId())
                .build();
    }
    public Pair<String, HttpStatus> register(UserDTO userDto) {
        User user = dtoToEntity(userDto);
        if (findByUserName(user.getUsername()) != null) return Pair.of("User already exists", HttpStatus.CONFLICT);
        userRepo.save(user);
        return Pair.of("Successful user registration", HttpStatus.OK);
    }
    public Pair<String, HttpStatus> logout(UserDTO user) {
        User userFound = findByUserName(user.getUsername());
        if (userFound == null) return Pair.of("User not found", HttpStatus.NOT_FOUND);
        userFound.setLoggedIn(false);
        userRepo.save(userFound);
        return Pair.of("Successful user logout", HttpStatus.OK);
    }
    public Pair<?, HttpStatus> getAllOnlineUsers() {
        List<User> userList = userRepo.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userList) {
            if(user.getLoggedIn())
                userDTOS.add(entityToDto(user));
        }
        return Pair.of(userDTOS, HttpStatus.OK);
    }
}
