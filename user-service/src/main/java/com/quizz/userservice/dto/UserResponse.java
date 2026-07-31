package com.quizz.userservice.dto;

import com.quizz.userservice.entity.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role
) {}