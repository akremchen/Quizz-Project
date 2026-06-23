package com.quizz.user_service.controller;

import com.quizz.user_service.dto.LoginRequest;
import com.quizz.user_service.dto.RegisterRequest;
import com.quizz.user_service.entity.User;
import com.quizz.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request);

            Map<String, Object> response = new HashMap<>();
            response.setStatus("success");
            response.put("userId", registeredUser.getId());
            response.put("username", registeredUser.getUsername());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User loggedInUser = userService.loginUser(request);

            Map<String, Object> response = new HashMap<>();
            response.setStatus("success");
            response.put("userId", loggedInUser.getId());
            response.put("username", loggedInUser.getUsername());
            response.put("email", loggedInUser.getEmail());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}
