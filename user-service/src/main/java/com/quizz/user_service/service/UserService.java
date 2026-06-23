package com.quizz.user_service.service;

import com.quizz.user_service.dto.LoginRequest;
import com.quizz.user_service.dto.RegisterRequest;
import com.quizz.user_service.entity.User;
import com.quizz.user_service.entity.UserProgress;
import com.quizz.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        UserProgress progress = UserProgress.builder()
                .user(user)
                .totalPoints(0)
                .totalBadges(0)
                .quizzesPublished(0)
                .quizzesCompleted(0)
                .build();

        user.setProgress(progress);
        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public User loginUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(request.getUsername());
        }
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid username,email, or password!");
        }
        return userOptional.get();

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid username,email, or password!");
        }
        return user;
    }
}
