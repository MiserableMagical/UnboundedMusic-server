package com.server.service;

import com.server.entity.User;
import com.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // token -> userId
    private final Map<String, String> tokenStore = new HashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        }

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, user.getId());

        return token;
    }

    public String getUserIdByToken(String token) {
        return tokenStore.get(token);
    }
}