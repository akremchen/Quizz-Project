package com.quizz.quizservice.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuizResultResponse {

    private Long quizId;
    private Long userId;
    private int score;
    private int totalQuestions;
    private int correctAnswers;
}
