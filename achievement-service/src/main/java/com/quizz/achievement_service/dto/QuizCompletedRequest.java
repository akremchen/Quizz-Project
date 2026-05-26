package com.quizz.achievement_service.dto;

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
    private Long userId;
    private int correctAnswers;
    private int totalQuestions;
    private String streakMilestone;
}
