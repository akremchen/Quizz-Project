package com.quizz.userservice.service;

import com.quizz.userservice.entity.User;
import com.quizz.userservice.entity.UserFavoriteCategory;
import com.quizz.userservice.repository.UserFavoriteCategoryRepository;
import com.quizz.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteCategoryService {

    private final UserFavoriteCategoryRepository favoriteCategoryRepository;
    private final UserRepository userRepository;

    public UserFavoriteCategoryService(
            UserFavoriteCategoryRepository favoriteCategoryRepository,
            UserRepository userRepository
    ) {
        this.favoriteCategoryRepository = favoriteCategoryRepository;
        this.userRepository = userRepository;
    }

    public void addFavoriteCategory(
            String email,
            String category
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        String normalizedCategory = category.trim();

        if (favoriteCategoryRepository
                .existsByUserIdAndCategoryIgnoreCase(
                        user.getId(),
                        normalizedCategory
                )) {
            return;
        }

        UserFavoriteCategory favorite =
                UserFavoriteCategory.builder()
                        .userId(user.getId())
                        .category(normalizedCategory)
                        .build();

        favoriteCategoryRepository.save(favorite);
    }

    public List<String> getFavoriteCategories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return favoriteCategoryRepository.findByUserId(user.getId())
                .stream()
                .map(UserFavoriteCategory::getCategory)
                .toList();
    }

    @Transactional
    public void removeFavoriteCategory(
            String email,
            String category
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        favoriteCategoryRepository
                .deleteByUserIdAndCategoryIgnoreCase(
                        user.getId(),
                        category.trim()
                );
    }

    public List<Long> getUserIdsByCategory(String category) {
        return favoriteCategoryRepository
                .findByCategoryIgnoreCase(category.trim())
                .stream()
                .map(UserFavoriteCategory::getUserId)
                .toList();
    }
}