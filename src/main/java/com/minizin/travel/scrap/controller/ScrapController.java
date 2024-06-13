package com.minizin.travel.scrap.controller;

import com.minizin.travel.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/scraps")
    public ResponseEntity<?> selectListScrapedPlans() {

        var result = scrapService.selectListScrapedPlans();

        return ResponseEntity.ok(result);
    }
    // #50 스크랩 조회 END //
}
