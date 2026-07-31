package com.quizz.quizservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8085")
public interface UserClient {

    @PostMapping("/api/users/{userId}/deduct-points")
    void deductPoints(@PathVariable("userId") Long userId, @RequestParam("points") int points);
}