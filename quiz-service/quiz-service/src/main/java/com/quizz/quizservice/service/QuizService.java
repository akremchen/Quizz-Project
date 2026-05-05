package com.quizz.quizservice.service;

import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public Quiz createQuiz(CreateQuizRequest request) {
        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .published(false)
                .build();
        return quizRepository.save(quiz);
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
}
