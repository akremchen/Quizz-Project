package com.quizz.quizservice.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuizResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Boolean published;

    private List<QuestionResponse> questions;
}
