package com.server.controller;

import com.server.dto.AuthRequest;
import com.server.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.login(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        authService.register(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok("Register success");
    }
}