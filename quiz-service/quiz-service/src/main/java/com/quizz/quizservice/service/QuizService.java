package com.quizz.quizservice.service;

import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.entity.AnswerOption;
import com.quizz.quizservice.entity.Question;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz createQuiz(CreateQuizRequest request) {

        //create quiz object
        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .published(false)
                .createdAt(LocalDateTime.now())
                .build();

        List<Question> questions = request.getQuestions().stream()
                .map(questionRequest -> {
                    Question question = Question.builder()
                            .question(questionRequest.getQuestion())
                            .quiz(quiz)
                            .build();

                    List<AnswerOption> answers = questionRequest.getOptions().stream()
                            .map(optionRequest -> AnswerOption.builder()
                                    .answer(optionRequest.getAnswer())
                                    .correct(optionRequest.isCorrect())
                                    .question(question)
                                    .build())
                            .toList();

                    question.setAnswers(answers);

                    return question;
                })
                .toList();

        quiz.setQuestions(questions);

        return quizRepository.save(quiz);
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
}