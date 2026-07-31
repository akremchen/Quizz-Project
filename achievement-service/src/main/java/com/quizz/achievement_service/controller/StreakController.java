package com.quizz.achievement_service.controller;

import com.quizz.achievement_service.dto.UserStreakResponse;
import com.quizz.achievement_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/achievement/streak")
@RequiredArgsConstructor
public class StreakController {

    private final StreakService streakService;

    @GetMapping
    public ResponseEntity<UserStreakResponse> getMyStreak(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return ResponseEntity.ok(
                streakService.getUserStreak(userId)
        );
    }
}