package com.quizz.userservice.dto;

public record ErrorResponse(
        int status,
        String message
) {
}