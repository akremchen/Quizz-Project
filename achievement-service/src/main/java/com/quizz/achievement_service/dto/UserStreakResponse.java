package com.quizz.achievement_service.dto;

public record UserStreakResponse(
        Long userId,
        int currentStreak,
        int longestStreak
) {
}