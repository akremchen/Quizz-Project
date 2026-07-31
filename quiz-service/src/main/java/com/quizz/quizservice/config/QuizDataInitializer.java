package com.quizz.quizservice.config;

import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.repository.QuizRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class QuizDataInitializer {

    @Bean
    CommandLineRunner initDatabase(QuizRepository quizRepository) {
        return args -> {
            if (quizRepository.count() == 0) {
                List<Quiz> premiumQuizzes = List.of(
                        Quiz.builder()
                                .title("Advanced Distributed Systems Architecture")
                                .description("Master the core concepts of fault tolerance and consistency.")
                                .category("Distributed Systems")
                                .published(true)
                                .isPremium(true)
                                .unlockPoints(100)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Quiz.builder()
                                .title("Kubernetes & Docker Mastery")
                                .description("Test your containerization and orchestration skills.")
                                .category("DevOps")
                                .published(true)
                                .isPremium(true)
                                .unlockPoints(150)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Quiz.builder()
                                .title("Spring Boot 4 Deep Dive")
                                .description("Explore advanced enterprise features in modern Spring framework.")
                                .category("Backend")
                                .published(true)
                                .isPremium(true)
                                .unlockPoints(120)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Quiz.builder()
                                .title("Microservice Security & JWT")
                                .description("Secure your distributed architecture using tokens and encryption.")
                                .category("Security")
                                .published(true)
                                .isPremium(true)
                                .unlockPoints(200)
                                .createdAt(LocalDateTime.now())
                                .build(),
                        Quiz.builder()
                                .title("Kafka Event Streaming Patterns")
                                .description("Learn event-driven architecture and message broker mechanics.")
                                .category("Messaging")
                                .published(true)
                                .isPremium(true)
                                .unlockPoints(250)
                                .createdAt(LocalDateTime.now())
                                .build()
                );

                quizRepository.saveAll(premiumQuizzes);
            }
        };
    }
}