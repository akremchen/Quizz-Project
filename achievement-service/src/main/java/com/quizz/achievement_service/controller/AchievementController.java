package com.quizz.achievement_service.controller;

import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.dto.UserBadgesResponse;
import com.quizz.achievement_service.dto.UserPointsResponse;
import com.quizz.achievement_service.service.AchievementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Void> processQuizCompletion(
            @Valid @RequestBody QuizCompletedRequest request
    ) {
        achievementService.processQuizCompletion(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/points")
    public ResponseEntity<UserPointsResponse> getMyPoints(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return ResponseEntity.ok(
                achievementService.getUserPoints(userId)
        );
    }

    @GetMapping("/badges")
    public ResponseEntity<UserBadgesResponse> getMyBadges(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return ResponseEntity.ok(
                achievementService.getUserBadges(userId)
        );
    }
}