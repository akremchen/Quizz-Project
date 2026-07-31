package com.quizz.achievement_service.controller;

import com.quizz.achievement_service.entity.UserStreak;
import com.quizz.achievement_service.service.StreakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {

    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }

    @PostMapping("/record")
    public ResponseEntity<UserStreak> recordStreak(@RequestParam Long userId) {
        UserStreak updatedStreak = streakService.recordQuizPlayed(userId);
        return ResponseEntity.ok(updatedStreak);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserStreak> getStreak(@PathVariable Long userId) {
        return streakService.getStreakByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}