package com.quizz.userservice.dto;

public record RegisterUserRequest(
        String username,
        String email,
        String password
) {
}