package com.quizz.quizservice.service;

import com.quizz.quizservice.dto.SubmitQuizRequest;
import com.quizz.quizservice.dto.UpdateQuizRequest;
import com.quizz.quizservice.dto.response.AnswerOptionResponse;
import com.quizz.quizservice.dto.CreateQuizRequest;
import com.quizz.quizservice.dto.response.QuestionResponse;
import com.quizz.quizservice.dto.response.QuizResponse;
import com.quizz.quizservice.dto.response.QuizResultResponse;
import com.quizz.quizservice.entity.AnswerOption;
import com.quizz.quizservice.entity.Question;
import com.quizz.quizservice.entity.Quiz;
import com.quizz.quizservice.entity.QuizAttempt;
import com.quizz.quizservice.exception.BadRequestException;
import com.quizz.quizservice.exception.ResourceNotFoundException;
import com.quizz.quizservice.kafka.QuizEventProducer;
import com.quizz.quizservice.repository.QuizAttemptRepository;
import com.quizz.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizEventProducer quizEventProducer;

    public Quiz createQuiz(CreateQuizRequest request) {

        //create quiz object
        Quiz quiz = Quiz.builder()
                .ownerId(request.getOwnerId())
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

    private QuizResponse mapToQuizResponse(Quiz quiz) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .ownerId(quiz.getOwnerId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .category(quiz.getCategory())
                .published(quiz.getPublished())
                .questions(
                        quiz.getQuestions().stream()
                                .map(question -> QuestionResponse.builder()
                                        .id(question.getId())
                                        .question(question.getQuestion())
                                        .options(
                                                question.getAnswers().stream()
                                                        .map(answer -> AnswerOptionResponse.builder()
                                                                .id(answer.getId())
                                                                .answer(answer.getAnswer())
                                                                .build()
                                                        )
                                                        .toList()
                                        )
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

    public List<QuizResponse> findAllQuizzes() {

        return quizRepository.findAll()
                .stream().map(this::mapToQuizResponse)
                .toList();
    }

    public QuizResponse findQuizById(Long id) {

        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Quiz found with id " + id));
        return mapToQuizResponse(quiz);
    }

    public QuizResponse publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Quiz found with id " + id));
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToQuizResponse(savedQuiz);
    }

    public QuizResultResponse submitQuiz(Long quizId, SubmitQuizRequest request) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() ->
                new ResourceNotFoundException("Quiz not found"));

        if (!Boolean.TRUE.equals(quiz.getPublished())) {
            throw new BadRequestException("Quiz must be published before it can be submitted");
        }

        int correctAnswers = 0;

        for (SubmitQuizRequest.QuestionAnswerRequest submittedAnswer  : request.getAnswers()) {

            Question question = quiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(submittedAnswer.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new BadRequestException("Question does not belong to this quiz: " + submittedAnswer.getQuestionId()));

            boolean isCorrect = question.getAnswers().stream()
                    .anyMatch(answer -> answer.getId().equals(submittedAnswer.getSelectedOptionId())
                    && answer.isCorrect());

            if (isCorrect) {
                correctAnswers++;
            }
        }

        QuizAttempt attempt = QuizAttempt.builder()
                .quizId(quizId)
                .userId(request.getUserId())
                .score(correctAnswers)
                .totalQuestions(quiz.getQuestions().size())
                .correctAnswers(correctAnswers)
                .submittedAt(LocalDateTime.now())
                .build();

        quizAttemptRepository.save(attempt);

        return QuizResultResponse.builder()
                .quizId(quizId)
                .userId(request.getUserId())
                .score(correctAnswers)
                .totalQuestions(quiz.getQuestions().size())
                .correctAnswers(correctAnswers)
                .build();
    }

    public QuizResponse updateQuiz(Long quizId, Long ownerId, UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Quiz not found with id: " + quizId));

        if (!quiz.getOwnerId().equals(ownerId)) {
            throw new BadRequestException("Only the quiz owner can update this quiz");
        }

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCategory(request.getCategory());

        quiz.getQuestions().clear();

        List<Question> updatedQuestions = request.getQuestions().stream()
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

        quiz.getQuestions().addAll(updatedQuestions);

        Quiz savedQuiz = quizRepository.save(quiz);

        return mapToQuizResponse(savedQuiz);
    }

    public void deleteQuiz(Long quizId, Long ownerId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        if(!ownerId.equals(quiz.getOwnerId())) {
            throw new BadRequestException("Only the quiz owner can delete this quiz");
        }

        quizRepository.delete(quiz);
    }

    public List<QuizResponse> findQuizzesByCategory(String category) {
        return quizRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToQuizResponse)
                .toList();
    }

    public List<QuizAttempt> getAttemptsByUserId(Long userId) {
        return quizAttemptRepository.findByUserId(userId);
    }
}