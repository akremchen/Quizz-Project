package com.quizz.achievement_service.controller;

import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.dto.UserBadgesResponse;
import com.quizz.achievement_service.dto.UserPointsResponse;
import com.quizz.achievement_service.service.AchievementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/achievement")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    @GetMapping("/ping")
    public String ping() {
        return "Achievement Service is up and running!";
    }

    @PostMapping("/quiz-completed")
    public ResponseEntity<String> processQuizCompletion(@Valid @RequestBody QuizCompletedRequest request) {
        achievementService.processQuizCompletion(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/points")
    public ResponseEntity<UserPointsResponse> getUserPoints(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.getUserPoints(userId));
    }

    @GetMapping("/{userId}/badges")
    public ResponseEntity<UserBadgesResponse> getUserBadges(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.getUserBadges(userId));
    }
}