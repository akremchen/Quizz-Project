package com.quizz.quizservice.service;

import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.entity.AnswerOption;
import com.quizz.quizservice.entity.Question;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        List<Question> questions = request.getQuestions().stream()
                .map(questionRequest -> {
                    Question question = Question.builder()
                            .question(questionRequest.getQuestion())
                            .quiz(quiz)
                            .build();
                    List<AnswerOption> options = questionRequest.getOptions().stream()
                            .map(optionRequest -> AnswerOption.builder()
                                    .answer(optionRequest.getAnswer())
                                    .correct(optionRequest.isCorrect())
                                    .question(question))

                })

        return quizRepository.save(quiz);
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
}
