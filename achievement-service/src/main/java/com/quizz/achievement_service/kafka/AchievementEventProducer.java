package com.quizz.achievement_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AchievementEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishBadgeEarned(Long userId, String badgeName) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("userId", userId.toString());
            event.put("badgeName", badgeName);

            kafkaTemplate.send(
                    "badge-earned",
                    objectMapper.writeValueAsString(event)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish badge-earned event", e);
        }
    }
}