package com.quizz.quizservice.repository;

import com.quizz.quizservice.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCategoryIgnoreCase(String category);
}
