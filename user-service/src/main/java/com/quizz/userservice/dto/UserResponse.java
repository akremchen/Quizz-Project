package com.quizz.userservice.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}