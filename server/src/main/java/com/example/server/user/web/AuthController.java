package com.example.server.user.web;

import com.example.server.user.domain.User;
import com.example.server.user.service.UserService;
import com.example.server.user.web.dto.AuthResponse;
import com.example.server.user.web.dto.LoginRequest;
import com.example.server.user.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request.getUserId(), request.getPassword(), request.getName(), request.getAge());
        return ResponseEntity.ok(AuthResponse.success(user.getUserId(), user.getName(), user.getAge()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUserId(), request.getPassword());
        return ResponseEntity.ok(AuthResponse.success(user.getUserId(), user.getName(), user.getAge()));
    }
}
