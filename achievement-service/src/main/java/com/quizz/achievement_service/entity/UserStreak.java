package com.quizz.achievement_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_streaks")
public class UserStreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private LocalDate streakStartDate;
    private LocalDate lastActiveDate;
    private int longestStreak = 0;

    public UserStreak() {}

    public UserStreak(Long userId, LocalDate streakStartDate, LocalDate lastActiveDate) {
        this.userId = userId;
        this.streakStartDate = streakStartDate;
        this.lastActiveDate = lastActiveDate;
    }

    public int getCurrentStreak() {
        if (streakStartDate == null) return 0;
        return (int) (lastActiveDate.toEpochDay() - streakStartDate.toEpochDay() + 1);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getStreakStartDate() { return streakStartDate; }
    public void setStreakStartDate(LocalDate streakStartDate) { this.streakStartDate = streakStartDate; }

    public LocalDate getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDate lastActiveDate) { this.lastActiveDate = lastActiveDate; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
}