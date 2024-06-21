package com.minizin.travel.plan.controller;

import com.minizin.travel.plan.service.OtherPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OtherPlanController {

    private final OtherPlanService otherPlanService;


    // #48 2024.06.10 다른 사람 여행 일정 조회 START //
    @GetMapping("/plans/others/newest")
    public ResponseEntity<?> selectOthersListPlan(@RequestParam("cursor_id") Long lastPlanId,
                                                  @RequestParam("region") String region,
                                                  @RequestParam("theme") String theme,
                                                  @RequestParam("search") String search) {

        var result = otherPlanService.selectOthersListPlan(lastPlanId, region, theme, search);

        return ResponseEntity.ok(result);
    }
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    @GetMapping("/plans/others/scraps")
    public ResponseEntity<?> selectOthersListPlanScraps(@RequestParam("cursor_id") Long lastPlanId,
                                                        @RequestParam("region") String region,
                                                        @RequestParam("theme") String theme,
                                                        @RequestParam("search") String search) {

        var result = otherPlanService.selectOthersListPlanScraps(lastPlanId, region, theme, search);

        return ResponseEntity.ok(result);
    }
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    // #106 2024.06.16 금주 인기 여행 일정 조회(북마크순) START //
    @GetMapping("/plans/popular/week")
    public ResponseEntity<?> selectPopularPlansWeek() {

        var result = otherPlanService.selectPopularPlansWeek();

        return ResponseEntity.ok(result);
    }
    // #106 2024.06.16 금주 인기 여행 일정 조회(북마크순) END //

    // #129 다른 사람의 여행 일정 상세 보기 START //
    @GetMapping("/plans/others/{plan_id}")
    public ResponseEntity<?> selectOtherDetailPlan(@PathVariable("plan_id") Long planId) {

        var result = otherPlanService.selectOtherDetailPlan(planId);

        return ResponseEntity.ok(result);
    }
    // #129 다른 사람의 여행 일정 상세 보기 END //
}

