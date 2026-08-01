package com.quizz.userservice.controller;

import com.quizz.userservice.dto.*;
import com.quizz.userservice.entity.User;
import com.quizz.userservice.security.JwtService;
import com.quizz.userservice.service.UserFavoriteCategoryService;
import com.quizz.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserFavoriteCategoryService favoriteCategoryService;



    public UserController(
            UserService userService,
            JwtService jwtService,
            UserFavoriteCategoryService favoriteCategoryService

    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.favoriteCategoryService = favoriteCategoryService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        User savedUser = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(toResponse(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        User user = userService.login(request);

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse(
                token,
                toResponse(user)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            Authentication authentication
    ) {
        String email = authentication.getName();

        User user = userService.getCurrentUser(email);

        return ResponseEntity.ok(toResponse(user));
    }

    @PutMapping("/me")
    public ResponseEntity<LoginResponse> updateMyProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        String currentEmail = authentication.getName();

        User updatedUser = userService.updateProfile(
                currentEmail,
                request
        );

        String newToken = jwtService.generateToken(updatedUser);

        return ResponseEntity.ok(
                new LoginResponse(
                        newToken,
                        toResponse(updatedUser)
                )
        );
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        userService.changePassword(email, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/favorite-categories")
    public ResponseEntity<Void> addFavoriteCategory(
            @RequestBody FavoriteCategoryRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        favoriteCategoryService.addFavoriteCategory(
                email,
                request.category()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me/favorite-categories")
    public ResponseEntity<List<String>> getMyFavoriteCategories(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                favoriteCategoryService.getFavoriteCategories(email)
        );
    }

    @GetMapping("/favorite-categories/{category}/users")
    public ResponseEntity<List<Long>> getUsersByFavoriteCategory(
            @PathVariable String category
    ) {
        return ResponseEntity.ok(
                favoriteCategoryService.getUserIdsByCategory(category)
        );
    }

    @DeleteMapping("/me/favorite-categories/{category}")
    public ResponseEntity<Void> removeFavoriteCategory(
            @PathVariable String category,
            Authentication authentication
    ) {
        String email = authentication.getName();

        favoriteCategoryService.removeFavoriteCategory(
                email,
                category
        );

        return ResponseEntity.noContent().build();
    }
}