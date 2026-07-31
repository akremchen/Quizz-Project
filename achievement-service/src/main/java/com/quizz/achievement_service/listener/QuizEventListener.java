package com.quizz.achievement_service.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.achievement_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizEventListener {

    private final StreakService streakService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "quiz-completed", groupId = "achievement-service-group")
    public void handleQuizCompleted(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            Long userId = event.get("userId").asLong();

            log.info("Received quiz-completed event for userId: {}", userId);

            streakService.recordQuizPlayed(userId);

        } catch (Exception e) {
            log.error("Failed to process quiz-completed event: {}", e.getMessage(), e);
        }
    }
}