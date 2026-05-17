package com.quizz.achievement_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/achievement")
public class AchievementController {
    @GetMapping("/ping")
    public String ping() {
        return "Achievement Service is up and running";
    }
}
