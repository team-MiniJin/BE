package com.minizin.travel.plan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minizin.travel.plan.dto.PlanDto;
import com.minizin.travel.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // #28 2024.05.30 내 여행 일정 생성하기 START //
    @PostMapping("/plans")
    public ResponseEntity<?> createPlan(
            @RequestBody PlanDto request
    ) throws JsonProcessingException {

        var result = planService.createPlan(request);

        return ResponseEntity.ok(result);

    }
    // #28 2024.05.30 내 여행 일정 생성하기 END //

    // #29 2024.06.02 내 여행 일정 조회 START //
    @GetMapping("/plans")
    public ResponseEntity<?> selectPlan(@RequestParam("cursor_id") Long cursorId) {

        var result = planService.selectList(cursorId);

        return ResponseEntity.ok(result);
    }
    // #29 2024.06.02 내 여행 일정 조회 END //
}

