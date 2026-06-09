package com.quizz.achievement_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementKafkaConsumer {

    private final AchievementService achievementService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "quiz-completed", groupId = "achievement-service-group")
    public void handleQuizCompleted(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);

            QuizCompletedRequest request = QuizCompletedRequest.builder()
                    .userId(Long.valueOf(payload.get("userId").toString()))
                    .correctAnswers(Integer.parseInt(payload.get("correctAnswers").toString()))
                    .totalQuestions(Integer.parseInt(payload.get("totalQuestions").toString()))
                    .streakMilestone(null)
                    .build();

            achievementService.processQuizCompletion(request);

            log.info("Handled quiz-completed event for user {}", request.getUserId());
        } catch (Exception e) {
            log.error("Failed to process quiz-completed message: {}", message, e);
        }
    }
}