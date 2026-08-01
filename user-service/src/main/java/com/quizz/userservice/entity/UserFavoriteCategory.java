package com.quizz.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "user_favorite_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "category"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoriteCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String category;
}