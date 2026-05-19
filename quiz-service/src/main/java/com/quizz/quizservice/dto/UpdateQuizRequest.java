package com.quizz.quizservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateQuizRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotEmpty(message = "Questions cannot be empty")
    @Valid
    private List<QuestionRequest> questions;

    @Getter
    @Setter
    public static class QuestionRequest {
        @NotBlank(message = "Question is required")
        private String question;

        @NotEmpty(message = "Options cannot be empty")
        @Valid
        private List<AnswerRequest> options;
    }

    @Getter
    @Setter
    public static class AnswerRequest {
        @NotBlank(message = "Answer is required")
        private String answer;

        private boolean correct;
    }
}