package com.quizz.quizservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "achievement-service",
        url = "http://achievement-service:8083"
)
public interface AchievementClient {

    @PostMapping("/api/achievement/users/{userId}/deduct-points")
    void deductPoints(
            @PathVariable("userId") Long userId,
            @RequestParam("points") int points
    );
}