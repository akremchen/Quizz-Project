package com.quizz.notification_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")

public interface UserClient {

    @GetMapping("/api/users/favorite-categories/{category}/users")
    List<Long> getUserIdsByFavoriteCategory(
            @PathVariable("category") String category
    );
}