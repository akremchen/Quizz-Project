package com.quizz.quizservice.repository;

import com.quizz.quizservice.entity.UserUnlockedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserUnlockedQuizRepository extends JpaRepository<UserUnlockedQuiz, Long> {
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);
}