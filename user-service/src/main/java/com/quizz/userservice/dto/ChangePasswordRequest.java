package com.quizz.userservice.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}