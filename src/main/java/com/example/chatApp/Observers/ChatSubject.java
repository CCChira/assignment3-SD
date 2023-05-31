package com.example.chatApp.Observers;

import com.example.chatApp.User.UserDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatSubject {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver(UserDTO userDTO) {
        for(Observer observer: observers) {
            observer.update(userDTO);
        }
    }
}
