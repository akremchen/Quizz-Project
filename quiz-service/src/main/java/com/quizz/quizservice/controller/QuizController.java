package com.quizz.quizservice.controller;


import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.dto.SubmitQuizRequest;
import com.quizz.quizservice.dto.UpdateQuizRequest;
import org.springframework.http.ResponseEntity;
import com.quizz.quizservice.dto.response.QuizResponse;
import com.quizz.quizservice.dto.response.QuizResultResponse;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.entity.QuizAttempt;
import com.quizz.quizservice.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public Quiz createQuiz(
            @Valid @RequestBody CreateQuizRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return quizService.createQuiz(request, userId);
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
    public QuizResponse publishQuiz(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return quizService.publishQuiz(id, userId);
    }

    @PostMapping("/{id}/submit")
    public QuizResultResponse submitQuiz(
            @PathVariable Long id,
            @Valid @RequestBody SubmitQuizRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return quizService.submitQuiz(id, request, userId);
    }

    @GetMapping("/attempts")
    public List<QuizAttempt> getMyAttempts(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return quizService.getAttemptsByUserId(userId);
    }

    @PutMapping("/{id}")
    public QuizResponse updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuizRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return quizService.updateQuiz(id, userId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        quizService.deleteQuiz(id, userId);
    }

    @GetMapping("/category/{category}")
    public List<QuizResponse> findQuizzesByCategory(@PathVariable String category) {
        return quizService.findQuizzesByCategory(category);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<QuizResponse>> getPremiumQuizzes() {
        return ResponseEntity.ok(quizService.getPremiumQuizzes());
    }

    @PostMapping("/{quizId}/unlock")
    public ResponseEntity<String> unlockQuiz(
            @PathVariable Long quizId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();
        quizService.unlockQuiz(quizId, userId);
        return ResponseEntity.ok("Quiz unlocked successfully!");
    }
}
