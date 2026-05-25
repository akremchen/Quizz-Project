package com.quizz.achievement_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizCompletedRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @Min(value = 0, message = "Correct answers cannot be negative")
    private int correctAnswers;

    @Min(value = 1, message = "Total questions must be at least 1")
    private int totalQuestions;

    private String streakMilestone;
}
