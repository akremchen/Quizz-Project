package com.quizz.userservice.service;

import com.quizz.userservice.dto.ChangePasswordRequest;
import com.quizz.userservice.dto.LoginRequest;
import com.quizz.userservice.dto.RegisterUserRequest;
import com.quizz.userservice.dto.UpdateProfileRequest;
import com.quizz.userservice.entity.Role;
import com.quizz.userservice.entity.User;
import com.quizz.userservice.exception.InvalidCredentialsException;
import com.quizz.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        // Users cannot register themselves as administrators.
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    public User updateProfile(
            String currentEmail,
            UpdateProfileRequest request
    ) {
        User user = getCurrentUser(currentEmail);

        if (request.username() == null ||
                request.username().isBlank()) {
            throw new RuntimeException("Username cannot be empty");
        }

        if (request.email() == null ||
                request.email().isBlank()) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (userRepository.existsByUsernameAndIdNot(
                request.username(),
                user.getId()
        )) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmailAndIdNot(
                request.email(),
                user.getId()
        )) {
            throw new RuntimeException("Email already exists");
        }

        user.setUsername(request.username());
        user.setEmail(request.email());

        return userRepository.save(user);
    }

    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {
        User user = getCurrentUser(email);

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException();
        }

        if (request.newPassword() == null ||
                request.newPassword().isBlank()) {
            throw new RuntimeException(
                    "New password cannot be empty"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        userRepository.save(user);
    }
}