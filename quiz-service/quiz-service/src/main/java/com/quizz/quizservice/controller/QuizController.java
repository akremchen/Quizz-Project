package com.quizz.quizservice.controller;


import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.dto.SubmitQuizRequest;
import com.quizz.quizservice.dto.response.QuizResponse;
import com.quizz.quizservice.dto.response.QuizResultResponse;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.entity.QuizAttempt;
import com.quizz.quizservice.service.QuizService;
import jakarta.validation.Valid;
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
    public List<QuizResponse> findAllQuizzes() {
        return quizService.findAllQuizzes();
    }

    @GetMapping("/{id}")
    public QuizResponse findQuizById(@PathVariable Long id) {
        return quizService.findQuizById(id);
    }

    @PatchMapping("/{id}/publish")
    public QuizResponse publishQuiz(@PathVariable Long id) {
        return quizService.publishQuiz(id);
    }

    @PostMapping("/{id}/submit")
    public QuizResultResponse submitQuiz(
        @PathVariable Long id,
        @Valid @RequestBody SubmitQuizRequest request)
    {
        return quizService.submitQuiz(id, request);
    }

    @GetMapping("/users/{userId}/attempts")
    public List<QuizAttempt> getAttemptsByUserId(@PathVariable Long userId) {
        return quizService.getAttemptsByUserId(userId);
    }
}
