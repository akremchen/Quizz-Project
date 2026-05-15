package com.quizz.quizservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerOptionResponse {
    private Long id;
    private String answer;
}
