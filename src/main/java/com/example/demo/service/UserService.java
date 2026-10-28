package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    public User register(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (userStore.containsKey(request.getUsername().trim())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setName(request.getName() == null ? "" : request.getName().trim());
        user.setEmail(request.getEmail() == null ? "" : request.getEmail().trim());
        user.setPhone(request.getPhone() == null ? "" : request.getPhone().trim());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userStore.put(user.getUsername(), user);
        return user;
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        User user = userStore.get(username.trim());
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password.");
        }
        return user;
    }

    public User findById(String id) {
        for (User user : userStore.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
