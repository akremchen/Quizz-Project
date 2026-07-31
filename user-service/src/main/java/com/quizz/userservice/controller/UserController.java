package com.quizz.userservice.controller;

import com.quizz.userservice.dto.LoginRequest;
import com.quizz.userservice.dto.LoginResponse;
import com.quizz.userservice.dto.RegisterUserRequest;
import com.quizz.userservice.dto.UserResponse;
import com.quizz.userservice.entity.User;
import com.quizz.userservice.security.JwtService;
import com.quizz.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    public UserController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        User savedUser = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(toResponse(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        User user = userService.login(request);

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse(
                token,
                toResponse(user)
        );

        return ResponseEntity.ok(response);
    }
}