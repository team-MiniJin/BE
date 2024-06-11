package com.minizin.travel.plan;

import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.service.PlanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectListPlanTest {

    private final PlanRepository planRepository;
    private final PlanService planService;

    @Autowired
    public SelectListPlanTest(PlanRepository planRepository, PlanService planService) {
        this.planRepository = planRepository;
        this.planService = planService;
    }

    @Test
    void selectListPlanTest() {
        // given
        for (int i = 0; i < 10; i++) {
            planRepository.save(Plan.builder()
                    .planName("test")
                    .userId(1L)
                    .build());
        }

        // when
        Long cursorId = 10L;
        var result = planService.selectListPlan(cursorId);

        // then
        // cursorId 보다 작은 id 값부터 조회
        Assertions.assertEquals(9, result.getData().get(0).getId());
        // 현재 planService 에 지정된 조회되는 개수가 6개 (id: 9 ~ 4까지 조회)
        // nextCursor의 값은 4로 지정
        Assertions.assertEquals(4, result.getNextCursor());
    }
}
