package com.quizz.userservice.repository;

import com.quizz.userservice.entity.UserFavoriteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteCategoryRepository
        extends JpaRepository<UserFavoriteCategory, Long> {

    List<UserFavoriteCategory> findByUserId(Long userId);

    List<UserFavoriteCategory> findByCategoryIgnoreCase(String category);

    boolean existsByUserIdAndCategoryIgnoreCase(
            Long userId,
            String category
    );

    void deleteByUserIdAndCategoryIgnoreCase(
            Long userId,
            String category
    );
}