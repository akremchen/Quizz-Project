package com.quizz.userservice.dto;

public record UpdateProfileRequest(
        String username,
        String email
) {
}