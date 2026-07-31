package com.quizz.userservice.dto;

public record LoginResponse(
        String token,
        UserResponse user
) {
}