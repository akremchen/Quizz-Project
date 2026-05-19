package com.quizz.quizservice.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmitQuizRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<QuestionAnswerRequest> answers;

    @Getter
    @Setter
    public static class QuestionAnswerRequest {

        @NotNull(message = "Question id is required")
        private Long questionId;

        @NotNull(message = "Selected option id is required")
        private Long selectedOptionId;
    }
}
