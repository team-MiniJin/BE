package com.minizin.travel.like.repository;

import com.minizin.travel.like.entity.Likes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    // #52 2024.06.13 좋아요 생성 //
    Boolean existsByPlanIdAndUserId(Long planId, Long userId);

    // #53 2024.06.13 좋아요 조회 START //
    List<Likes> findAllByUserIdOrderByIdDesc(Long userId, Pageable page);

    List<Likes> findByIdLessThanAndUserIdOrderByIdDesc(Long cursorId, Long userId, Pageable page);

    Boolean existsByUserId(Long userId);

    // #53 2024.06.13 좋아요 조회 END //

}
