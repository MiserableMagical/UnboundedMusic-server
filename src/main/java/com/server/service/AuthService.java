package com.server.service;

import com.server.entity.User;
import com.server.exception.BusinessException;
import com.server.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    // token -> userId
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // ✅ 正确的密码校验方式
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, user.getId());
        return token;
    }

    public void register(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new BusinessException(400, "Username cannot be empty");
        }

        if (password == null || password.isBlank()) {
            throw new BusinessException(400, "Password cannot be empty");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new BusinessException(409, "Username already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public String getUserIdByToken(String token) {
        return tokenStore.get(token);
    }
}