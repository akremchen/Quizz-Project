package com.quizz.quizservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.dto.response.QuizResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishQuizCompleted(QuizResultResponse result) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("quizId", result.getQuizId());
            event.put("userId", result.getUserId());
            event.put("score", result.getScore());
            event.put("correctAnswers", result.getCorrectAnswers());
            event.put("totalQuestions", result.getTotalQuestions());

            kafkaTemplate.send("quiz-completed", objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish quiz-completed event", e);
        }
    }

    public void publishQuizPublished(Quiz quiz) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("quizId", quiz.getId());
            event.put("title", quiz.getTitle());
            event.put("category", quiz.getCategory());
            event.put("ownerId", quiz.getOwnerId());

            kafkaTemplate.send("quiz-published", objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish quiz-published event", e);
        }
    }
}