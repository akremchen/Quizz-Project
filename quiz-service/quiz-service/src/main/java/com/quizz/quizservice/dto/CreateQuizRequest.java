package com.quizz.quizservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuizRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    @NotEmpty
    private List<QuestionRequest> questions;

    @Getter
    @Setter
    public static class QuestionRequest {
        @NotBlank
        private String question;

        @NotEmpty
        private List<AnswerRequest> options;
    }

    @Getter
    @Setter
    public static class AnswerRequest {
        @NotBlank
        private String answer;

        private boolean correct;
    }
}