package com.quizz.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProgress {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private int totalPoints;
    private int totalBadges;
    private int quizzesPublished;
    private int quizzesCompleted;
}
