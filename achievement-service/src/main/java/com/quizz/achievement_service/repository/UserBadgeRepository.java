package com.quizz.achievement_service.repository;

import com.quizz.achievement_service.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUserId(Long userId);
    boolean existsByUserIdAndBadgeName(Long userId, String badgeName);
}
