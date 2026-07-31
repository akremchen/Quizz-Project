package com.quizz.achievement_service.kafka;

import com.quizz.achievement_service.entity.UserStreak;
import com.quizz.achievement_service.service.StreakService;
import tools.jackson.databind.ObjectMapper;
import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementKafkaConsumer {

    private final AchievementService achievementService;
    private final StreakService streakService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "quiz-completed",
            groupId = "achievement-service-group"
    )
    public void handleQuizCompleted(String message) {
        try {
            QuizCompletedRequest request =
                    objectMapper.readValue(
                            message,
                            QuizCompletedRequest.class
                    );

            achievementService.processQuizCompletion(request);

            UserStreak streak =
                    streakService.recordQuizPlayed(
                            request.getUserId()
                    );

            long scorePercentage = Math.round(
                    ((double) request.getCorrectAnswers()
                            / request.getTotalQuestions()) * 100
            );

            achievementService.processStreakMilestones(
                    request.getUserId(),
                    streak.getCurrentStreak(),
                    scorePercentage
            );

            log.info(
                    "Handled quiz-completed event for user {}",
                    request.getUserId()
            );

        } catch (Exception exception) {
            log.error(
                    "Failed to process quiz-completed event: {}",
                    message,
                    exception
            );
        }
    }
}