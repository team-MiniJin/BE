package com.minizin.travel.plan.controller;

import com.minizin.travel.plan.dto.EditPlanDto;
import com.minizin.travel.plan.dto.PlanDto;
import com.minizin.travel.plan.service.PlanService;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // #28 2024.05.30 내 여행 일정 생성하기 START //
    @PostMapping("/plans")
    public ResponseEntity<?> createPlan(
            @RequestBody @Valid PlanDto request, // @Valid : #87 Request 예외/에러 처리
            @AuthenticationPrincipal PrincipalDetails user)
            throws BadRequestException {

        var result = planService.createPlan(request, user);

        return ResponseEntity.ok(result);

    }
    // #28 2024.05.30 내 여행 일정 생성하기 END //

    // #29 2024.06.02 내 여행 일정 조회 START //
    @GetMapping("/plans")
    public ResponseEntity<?> selectListPlan(@RequestParam("cursor_id") Long lastPlanId,
                                            @AuthenticationPrincipal PrincipalDetails user) {  // #102 [GET] /plans : Refactoring - cursorId renaming

        var result = planService.selectListPlan(lastPlanId, user);

        return ResponseEntity.ok(result);
    }
    // #29 2024.06.02 내 여행 일정 조회 END //

    // #32 2024.06.07 내 여행 일정 수정 START //
    @PutMapping("/plans/{plan_id}")
    public ResponseEntity<?> updatePlan(@PathVariable("plan_id") Long planId,
                                        @RequestBody EditPlanDto request,
                                        @AuthenticationPrincipal PrincipalDetails user) {

        var result = planService.updatePlan(planId, request, user);

        return ResponseEntity.ok(result);
    }
    // #32 2024.06.07 내 여행 일정 수정 END //

    // #38 2024.06.08 내 여행 일정 상세 보기 START //
    @GetMapping("/plans/details/{plan_id}")
    public ResponseEntity<?> selectDetailPlan(@PathVariable("plan_id") Long planId,
                                              @AuthenticationPrincipal PrincipalDetails user) {

        var result = planService.selectDetailPlan(planId, user);

        return ResponseEntity.ok(result);
    }
    // #38 2024.06.08 내 여행 일정 상세 보기 END //

    // #47 2024.06.13 내 여행 일정 삭제 START //
    @DeleteMapping("/plans/{plan_id}")
    public ResponseEntity<?> deletePlan(@PathVariable("plan_id") Long planId,
                                        @AuthenticationPrincipal PrincipalDetails user) {

        var result = planService.deletePlan(planId, user);

        return ResponseEntity.ok(result);
    }
    // #47 2024.06.13 내 여행 일정 삭제 END //

    // #39 2024.06.10 다가오는 여행 일정 조회 START //
    @GetMapping("/plans/upcoming")
    public ResponseEntity<?> selectUpcomingPlan(@AuthenticationPrincipal PrincipalDetails user) {

        var result = planService.selectUpcomingPlan(user);

        return ResponseEntity.ok(result);
    }
    // #39 2024.06.10 다가오는 여행 일정 조회 END //

    // #107 2024.06.20 일정 복사하기 START //
    @GetMapping("/plans/copy/{plan_id}")
    public ResponseEntity<?> copyAndCreatePlan(@PathVariable("plan_id") Long planId,
                                               @AuthenticationPrincipal PrincipalDetails user) {

        var result = planService.copyAndCreatePlan(planId, user);

        return ResponseEntity.ok(result);
    }
    // #107 2024.06.20 일정 복사하기 END //
}

