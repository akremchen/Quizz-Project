package com.quizz.achievement_service.service;

import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.dto.UserBadgesResponse;
import com.quizz.achievement_service.dto.UserPointsResponse;
import com.quizz.achievement_service.entity.UserBadge;
import com.quizz.achievement_service.entity.UserPoints;
import com.quizz.achievement_service.exception.BadRequestException;
import com.quizz.achievement_service.kafka.AchievementEventProducer;
import com.quizz.achievement_service.repository.UserBadgeRepository;
import com.quizz.achievement_service.repository.UserPointsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserPointsRepository pointsRepository;
    private final UserBadgeRepository badgeRepository;
    private final AchievementEventProducer eventProducer;

    private static final Map<Integer, String> POINT_MILESTONES = new LinkedHashMap<>();
    static {
        POINT_MILESTONES.put(100000, "Point Titan earned 100000 Points");
        POINT_MILESTONES.put(50000, "Point Immortal earned 50000 Points");
        POINT_MILESTONES.put(20000, "Point Conqueror earned 20000 Points");
        POINT_MILESTONES.put(10000, "Point Champion earned 10000 Points");
        POINT_MILESTONES.put(5000, "Point Champion earned 5000 Points");
        POINT_MILESTONES.put(2500, "Point Master earned 2500 Points");
        POINT_MILESTONES.put(1000, "Point Elite earned 1000 Points");
        POINT_MILESTONES.put(500, "Point Challenger earned 500 Points");
        POINT_MILESTONES.put(100, "Point Rookie earned 100 Points");
    }

    public void processQuizCompletion(QuizCompletedRequest request) {
        if (request.getTotalQuestions() <= 0) {
            throw new BadRequestException("Total questions must be at least 1");
        }
        if (request.getCorrectAnswers() > request.getTotalQuestions()) {
            throw new BadRequestException("Correct answers cannot be more than total questions");
        }

        Long userId = request.getUserId();
        double rawPercentage = ((double) request.getCorrectAnswers() / request.getTotalQuestions()) * 100;
        long roundedScore = Math.round(rawPercentage);

        UserPoints userPoints = pointsRepository.findByUserId(userId)
                .orElse(UserPoints.builder().userId(userId).points(0).build());

        int oldPoints = userPoints.getPoints();

        if (roundedScore >= 50) {
            int pointsEarned = (int) roundedScore;
            userPoints.setPoints(oldPoints + pointsEarned);
            pointsRepository.save(userPoints);
            eventProducer.publishPointsEarned(userId, pointsEarned);
        }

        int totalPoints = userPoints.getPoints();

        if (oldPoints < 1000 && totalPoints >= 1000) {
            awardBadgeIfNotExists(userId, "Premium Content Explorer");
            eventProducer.publishPremiumUnlocked(userId, "TIER_1_PREMIUM");
        }

        awardBadgeIfNotExists(userId, "First Quiz Completed");

        if (roundedScore == 100) {
            awardBadgeIfNotExists(userId, "Perfect Score");
        }

        String streakMilestone = request.getStreakMilestone();
        boolean hasStreak = false;

        if ("MONTH".equalsIgnoreCase(streakMilestone)) {
            awardBadgeIfNotExists(userId, "Month Streak");
            hasStreak = true;
        } else if ("WEEK".equalsIgnoreCase(streakMilestone)) {
            awardBadgeIfNotExists(userId, "Week Streak");
            hasStreak = true;
        }

        if (roundedScore == 100 && hasStreak) {
            awardBadgeIfNotExists(userId, "Golden Streak");
        }

        if (request.getTotalQuestions() >= 10 && roundedScore == 100) {
            awardBadgeIfNotExists(userId, "Flawless Marathon");
        } else if (request.getCorrectAnswers() == 1 && request.getTotalQuestions() == 1) {
            awardBadgeIfNotExists(userId, "Speedrunner");
        }

        for (Map.Entry<Integer, String> entry : POINT_MILESTONES.entrySet()) {
            if (totalPoints >= entry.getKey()) {
                awardBadgeIfNotExists(userId, entry.getValue());
                break;
            }
        }
    }

    private void awardBadgeIfNotExists(Long userId, String badgeName) {
        if (!badgeRepository.existsByUserIdAndBadgeName(userId, badgeName)) {
            badgeRepository.save(
                    UserBadge.builder()
                            .userId(userId)
                            .badgeName(badgeName)
                            .build()
            );
            eventProducer.publishBadgeEarned(userId, badgeName);
        }
    }

    public UserPointsResponse getUserPoints(Long userId) {
        UserPoints points = pointsRepository.findByUserId(userId).orElse(null);
        int total = (points != null) ? points.getPoints() : 0;
        return UserPointsResponse.builder().userId(userId).points(total).build();
    }

    public UserBadgesResponse getUserBadges(Long userId) {
        List<UserBadge> badges = badgeRepository.findByUserId(userId);
        List<String> badgeNames = badges.stream().map(UserBadge::getBadgeName).toList();
        return UserBadgesResponse.builder().userId(userId).badges(badgeNames).build();
    }
}