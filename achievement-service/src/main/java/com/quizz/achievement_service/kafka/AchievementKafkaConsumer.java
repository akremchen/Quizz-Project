package com.quizz.achievement_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.achievement_service.dto.QuizCompletedRequest;
import com.quizz.achievement_service.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementKafkaConsumer {

    private final AchievementService achievementService;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(topics = "quiz-completed", groupId = "achievement-service-group")
    public void handleQuizCompleted(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);

            String eventId = payload.get("quizResultId") != null ? payload.get("quizResultId").toString()
                    : payload.get("quizId") + "-time-" + System.currentTimeMillis();

            String redisKey = "event:dedup:" + eventId;

            Boolean isNewEvent = redisTemplate.opsForValue().setIfAbsent(
                    redisKey,
                    "processed",
                    Duration.ofHours(24)
            );

            if(Boolean.FALSE.equals(isNewEvent)){
                log.warn("Duplicate event detected for eventId: {}", eventId);
                return;
            }

            String streak = payload.get("streakMilestone") != null ? payload.get("streakMilestone").toString() : null;
            String category = payload.get("category") != null ? payload.get("category").toString() : null;

            QuizCompletedRequest request = QuizCompletedRequest.builder()
                    .userId(Long.valueOf(payload.get("userId").toString()))
                    .correctAnswers(Integer.parseInt(payload.get("correctAnswers").toString()))
                    .totalQuestions(Integer.parseInt(payload.get("totalQuestions").toString()))
                    .streakMilestone(streak)
                    .category(category)
                    .build();

            achievementService.processQuizCompletion(request);

            log.info("Successfully processed unique event via Redis cache: {}", eventId);
        } catch (Exception e) {
            log.error("Failed to process quiz-completed message: {}", message, e);
        }
    }
}