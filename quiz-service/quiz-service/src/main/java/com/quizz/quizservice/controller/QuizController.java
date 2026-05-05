package com.quizz.quizservice.controller;


import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.service.QuizService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public Quiz createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request);
    }
    @GetMapping
    public List<Quiz> findAllQuizzes() {
        return quizService.findAllQuizzes();
    }

}
