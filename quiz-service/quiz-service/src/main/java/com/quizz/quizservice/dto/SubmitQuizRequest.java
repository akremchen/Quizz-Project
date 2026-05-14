package com.quizz.quizservice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmitQuizRequest {

    private Long userId;

    private List<QuestionAnswerRequest> answers;

    @Getter
    @Setter
    public static class QuestionAnswerRequest {
        private Long questionId;
        private Long selectedOptionId;
    }
}
