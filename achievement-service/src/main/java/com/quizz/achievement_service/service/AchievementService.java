package com.quizz.achievement_service.service;

import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.dto.UserBadgesResponse;
import com.quizz.achievement_service.dto.UserPointsResponse;
import com.quizz.achievement_service.entity.UserBadge;
import com.quizz.achievement_service.entity.UserPoints;
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
        Long userId = request.getUserId();
        double scorePercentage = (double) request.getCorrectAnswers() / request.getTotalQuestions();

        if (scorePercentage >= 0.5) {
            int pointsEarned = (int) (scorePercentage * 100);
            UserPoints userPoints = pointsRepository.findByUserId(userId)
                    .orElse(UserPoints.builder().userId(userId).points(0).build());
            userPoints.setPoints(userPoints.getPoints() + pointsEarned);
            pointsRepository.save(userPoints);
        }

        if (!badgeRepository.existsByUserIdAndBadgeName(userId, "First Quiz Completed")) {
            badgeRepository.save(UserBadge.builder().userId(userId).badgeName("First Quiz Completed").build());
        }

        if (request.getCorrectAnswers() >= request.getTotalQuestions()) {
            if (!badgeRepository.existsByUserIdAndBadgeName(userId, "Perfect Score")) {
                badgeRepository.save(UserBadge.builder().userId(userId).badgeName("Perfect Score").build());
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
