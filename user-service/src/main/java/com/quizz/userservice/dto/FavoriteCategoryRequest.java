package com.quizz.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteCategoryRequest(

        @NotBlank(message = "Category is required")
        String category
) {
}