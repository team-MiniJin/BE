package com.minizin.travel.scrap.controller;

import com.minizin.travel.scrap.service.ScrapService;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // #49 스크랩 생성 START //
    @PostMapping("/scraps/{plan_id}")
    public ResponseEntity<?> createScrap(@PathVariable("plan_id") Long planId,
                                         @AuthenticationPrincipal PrincipalDetails user) {

        var result = scrapService.createScrap(planId, user);

        return ResponseEntity.ok(result);
    }
    // #49 스크랩 생성 END //

    // #50 스크랩 조회 START //
    @GetMapping("/scraps")
    public ResponseEntity<?> selectListScrapedPlans(@RequestParam("cursor_id") Long cursorId,
                                                    @AuthenticationPrincipal PrincipalDetails user) {

        var result = scrapService.selectListScrapedPlans(cursorId, user);

        return ResponseEntity.ok(result);
    }
    // #50 스크랩 조회 END //

    // #51 스크랩 삭제 START //
    @DeleteMapping("/scraps/{plan_id}")
    public ResponseEntity<?> deleteScrapedPlan(@PathVariable("plan_id") Long planId,
                                               @AuthenticationPrincipal PrincipalDetails user) {

        var result = scrapService.deleteScrapedPlan(planId, user);

        return ResponseEntity.ok(result);
    }
    // #51 스크랩 삭제 END //

    @GetMapping("/scraps/check/{plan_id}")
    public ResponseEntity<?> checkScrapedPlan(@PathVariable("plan_id") Long planId,
                                              @AuthenticationPrincipal PrincipalDetails user) {

        var result = scrapService.checkScrapedPlan(planId, user);

        return ResponseEntity.ok(result);
    }
}
