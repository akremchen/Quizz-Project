package com.quizz.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.notification_service.entity.NotificationType;
import com.quizz.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "quiz-published", groupId = "notification-service-group")
    public void handleQuizPublished(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);
            UUID userId = UUID.fromString((String) payload.get("userId"));
            String quizTitle = (String) payload.get("quizTitle");

            notificationService.createNotification(
                    userId,
                    "New Quiz Published",
                    "A new quiz has been published: " + quizTitle,
                    NotificationType.QUIZ_PUBLISHED
            );

            log.info("Handled quiz-published event for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to process quiz-published message: {}", message, e);
        }
    }

    @KafkaListener(topics = "badge-earned", groupId = "notification-service-group")
    public void handleBadgeEarned(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);
            UUID userId = UUID.fromString((String) payload.get("userId"));
            String badgeName = (String) payload.get("badgeName");

            notificationService.createNotification(
                    userId,
                    "Badge Earned!",
                    "You earned a new badge: " + badgeName,
                    NotificationType.BADGE_EARNED
            );

            log.info("Handled badge-earned event for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to process badge-earned message: {}", message, e);
        }
    }

    @KafkaListener(topics = "points-earned", groupId = "notification-service-group")
    public void handlePointsEarned(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);
            UUID userId = UUID.fromString((String) payload.get("userId"));
            int points = (int) payload.get("points");

            notificationService.createNotification(
                    userId,
                    "Points Earned!",
                    "You earned " + points + " points!",
                    NotificationType.POINTS_EARNED
            );

            log.info("Handled points-earned event for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to process points-earned message: {}", message, e);
        }
    }
}