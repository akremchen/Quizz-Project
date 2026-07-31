package com.quizz.notification_service.controller;

import com.quizz.notification_service.entity.Notification;
import com.quizz.notification_service.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return ResponseEntity.ok(
                notificationService.getNotificationsForUser(userId)
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getMyUnreadNotifications(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        try {
            notificationService.markAsRead(
                    notificationId,
                    userId
            );

            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}