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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserPointsRepository pointsRepository;
    private final UserBadgeRepository badgeRepository;
    private final AchievementEventProducer eventProducer;

    public void processQuizCompletion(QuizCompletedRequest request) {
        if (request.getTotalQuestions() <= 0) {
            throw new BadRequestException("Total questions must be at least 1");
        }

        if (request.getCorrectAnswers() > request.getTotalQuestions()) {
            throw new BadRequestException("Correct answers cannot be more than total questions");
        }

        Long userId = request.getUserId();

        double rawPercentage =
                ((double) request.getCorrectAnswers() / request.getTotalQuestions()) * 100;

        long roundedScore = Math.round(rawPercentage);

        UserPoints userPoints = pointsRepository.findByUserId(userId)
                .orElse(UserPoints.builder()
                        .userId(userId)
                        .points(0)
                        .build());

        if (roundedScore >= 50) {
            int pointsEarned = (int) roundedScore;

            userPoints.setPoints(userPoints.getPoints() + pointsEarned);
            pointsRepository.save(userPoints);

            eventProducer.publishPointsEarned(userId, pointsEarned);
        }

        awardBadgeIfNotExists(userId, "First Quiz Completed");

        if (roundedScore == 100) {
            awardBadgeIfNotExists(userId, "Perfect Score");
        }

        String streakMilestone = request.getStreakMilestone();

        if ("MONTH".equalsIgnoreCase(streakMilestone)) {
            awardBadgeIfNotExists(userId, "Month Streak");
        } else if ("WEEK".equalsIgnoreCase(streakMilestone)) {
            awardBadgeIfNotExists(userId, "Week Streak");
        }

        if (roundedScore == 100 &&
                ("WEEK".equalsIgnoreCase(streakMilestone)
                        || "MONTH".equalsIgnoreCase(streakMilestone))) {
            awardBadgeIfNotExists(userId, "Golden Streak");
        }

        int totalPoints = userPoints.getPoints();

        if (totalPoints >= 100000) {
            awardBadgeIfNotExists(userId, "Point Titan earned 100000 Points");
        } else if (totalPoints >= 50000) {
            awardBadgeIfNotExists(userId, "Point Immortal earned 50000 Points");
        } else if (totalPoints >= 20000) {
            awardBadgeIfNotExists(userId, "Point Conqueror earned 20000 Points");
        } else if (totalPoints >= 10000) {
            awardBadgeIfNotExists(userId, "Point Champion earned 10000 Points");
        } else if (totalPoints >= 5000) {
            awardBadgeIfNotExists(userId, "Point Champion earned 5000 Points");
        } else if (totalPoints >= 2500) {
            awardBadgeIfNotExists(userId, "Point Master earned 2500 Points");
        } else if (totalPoints >= 1000) {
            awardBadgeIfNotExists(userId, "Point Elite earned 1000 Points");
        } else if (totalPoints >= 500) {
            awardBadgeIfNotExists(userId, "Point Challenger earned 500 Points");
        } else if (totalPoints >= 100) {
            awardBadgeIfNotExists(userId, "Point Rookie earned 100 Points");
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

        return UserPointsResponse.builder()
                .userId(userId)
                .points(total)
                .build();
    }

    public UserBadgesResponse getUserBadges(Long userId) {
        List<UserBadge> badges = badgeRepository.findByUserId(userId);

        List<String> badgeNames = badges.stream()
                .map(UserBadge::getBadgeName)
                .toList();

        return UserBadgesResponse.builder()
                .userId(userId)
                .badges(badgeNames)
                .build();
    }
}