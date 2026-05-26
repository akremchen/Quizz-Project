package com.quizz.quizservice.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String question;

    private List<AnswerOptionResponse> options;
}
