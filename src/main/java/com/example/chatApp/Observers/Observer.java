package com.example.chatApp.Observers;

import com.example.chatApp.User.UserDTO;

import java.util.ArrayList;

public interface Observer {
    void update(UserDTO userDTO);
}
