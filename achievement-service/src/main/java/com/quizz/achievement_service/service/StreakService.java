package com.quizz.achievement_service.service;

import com.quizz.achievement_service.entity.UserStreak;
import com.quizz.achievement_service.repository.UserStreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StreakService {

    private final UserStreakRepository streakRepository;

    public StreakService(UserStreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    public UserStreak recordQuizPlayed(Long userId) {
        LocalDate today = LocalDate.now();

        UserStreak streak = streakRepository.findByUserId(userId)
                .orElse(new UserStreak(userId, null, null));

        if (streak.getLastActiveDate() == null) {
            streak.setStreakStartDate(today);
        } else if (streak.getLastActiveDate().isBefore(today.minusDays(1))) {
            streak.setStreakStartDate(today);
        }

        streak.setLastActiveDate(today);

        int current = streak.getCurrentStreak();
        if (current > streak.getLongestStreak()) {
            streak.setLongestStreak(current);
        }

        return streakRepository.save(streak);
    }
    public Optional<UserStreak> getStreakByUserId(Long userId) {
        return streakRepository.findByUserId(userId);
    }
}