package com.minizin.travel.scrap.controller;

import com.minizin.travel.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // #49 스크랩 생성 START //
    @PostMapping("/scraps/{plan_id}")
    public ResponseEntity<?> createScrap(@PathVariable("plan_id") Long planId) {

        var result = scrapService.createScrap(planId);

        return ResponseEntity.ok(result);
    }
    // #49 스크랩 생성 END //

    // #50 스크랩 조회 START //
    @GetMapping("/scraps/{cursor_id}")
    public ResponseEntity<?> selectListScrapedPlans(@PathVariable("cursor_id") Long cursorId) {

        var result = scrapService.selectListScrapedPlans(cursorId);

        return ResponseEntity.ok(result);
    }
    // #50 스크랩 조회 END //

    // #51 스크랩 삭제 START //
    @DeleteMapping("/scraps/{scrap_id}")
    public ResponseEntity<?> deleteScrapedPlan(@PathVariable("scrap_id") Long scrapId) {

        var result = scrapService.deleteScrapedPlan(scrapId);

        return ResponseEntity.ok(result);
    }
    // #51 스크랩 삭제 END //
}
