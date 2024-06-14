package com.minizin.travel.like.controller;

import com.minizin.travel.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // #52 2024.06.13 좋아요 생성 START //
    @PostMapping("/likes/{plan_id}")
    public ResponseEntity<?> createLikePlan(@PathVariable("plan_id") Long planId) {

        var result = likeService.createLikePlan(planId);

        return ResponseEntity.ok(result);
    }
    // #52 2024.06.13 좋아요 생성 END //

    // #53 2024.06.13 좋아요 조회 START //
    @GetMapping("/likes/{cursor_id}")
    public ResponseEntity<?> selectListLikedPlans(@PathVariable("cursor_id") Long cursorId) {

        var result = likeService.selectListLikedPlans(cursorId);

        return ResponseEntity.ok(result);
    }
    // #53 2024.06.13 좋아요 조회 END //
}
