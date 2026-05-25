package com.quizz.achievement_service.service;

import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.dto.UserBadgesResponse;
import com.quizz.achievement_service.dto.UserPointsResponse;
import com.quizz.achievement_service.entity.UserBadge;
import com.quizz.achievement_service.entity.UserPoints;
import com.quizz.achievement_service.exception.BadRequestException;
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

        if (roundedScore >= 50) {
            int pointsEarned = (int) roundedScore;
            userPoints.setPoints(userPoints.getPoints() + pointsEarned);
            pointsRepository.save(userPoints);
        }

        if (!badgeRepository.existsByUserIdAndBadgeName(userId, "First Quiz Completed")) {
            badgeRepository.save(UserBadge.builder().userId(userId).badgeName("First Quiz Completed").build());
        }

        if (roundedScore == 100) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Perfect Score")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Perfect Score").build());
            }
        }

        String streakMilestone = request.getStreakMilestone();

        if ("MONTH".equals(streakMilestone)) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Month Streak")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Month Streak").build());
            }
        }
        else if ("WEEK".equalsIgnoreCase(streakMilestone)) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Week Streak")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Week Streak").build());
            }
        }

        if (roundedScore == 100 && ("WEEK".equalsIgnoreCase(streakMilestone) || "MONTH".equalsIgnoreCase(streakMilestone))) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Golden Streak")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Golden Streak").build());
            }
        }

        int totalPoints = userPoints.getPoints();
        if (totalPoints >= 100000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Titan earned 100000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Titan earned 100000 Points").build());
            }
        } else if (totalPoints >= 50000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Immortal earned 50000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Immortal earned 50000 Points").build());
            }
        } else if (totalPoints >= 20000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Conqueror earned 20000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Conqueror earned 20000 Points").build());
            }
        } else if (totalPoints >= 10000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Champion earned 10000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Champion earned 10000 Points").build());
            }
        } else if (totalPoints >= 5000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Champion earned 5000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Champion earned 5000 Points").build());
            }
        } else if (totalPoints >= 2500) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Master earned 2500 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Master earned 2500 Points").build());
            }
        } else if (totalPoints >= 1000) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Elite earned 1000 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Elite earned 1000 Points").build());
            }
        } else if (totalPoints >= 500) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Challenger earned 500 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Challenger earned 500 Points").build());
            }
        } else if (totalPoints >= 100) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Point Rookie earned 100 Points")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Point Rookie earned 100 Points").build());
            }
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
