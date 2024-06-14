package com.minizin.travel.plan;

import com.minizin.travel.plan.dto.PlanBudgetDto;
import com.minizin.travel.plan.dto.PlanDto;
import com.minizin.travel.plan.dto.PlanScheduleDto;
import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanBudget;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanBudgetRepository;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
public class CreatePlanTest {

    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final PlanBudgetRepository planBudgetRepository;
    final int INITIAL_VALUE = 0;

    @Autowired
    public CreatePlanTest(PlanRepository planRepository, PlanScheduleRepository planScheduleRepository, PlanBudgetRepository planBudgetRepository) {
        this.planRepository = planRepository;
        this.planScheduleRepository = planScheduleRepository;
        this.planBudgetRepository = planBudgetRepository;
    }

    @Test
    void createPlanTest() {
        // given
        // PlanDto
        PlanDto planDto = PlanDto.builder()
                .userId(1L)
                .planName("장소 계획 테스트")
                .theme("테마 계획 테스트")
                .startDate(LocalDate.parse("2024-06-10"))
                .endDate(LocalDate.parse("2024-07-01"))
                .scope(true)
                .numberOfMembers(3)
                .build();

        // PlanScheduleDto
        PlanScheduleDto planScheduleDto = PlanScheduleDto.builder()
                .scheduleDate(LocalDate.parse("2024-06-01"))
                .placeCategory("스케줄 카테고리 테스트")
                .placeName("스케줄 이름 테스트")
                .region("지역")
                .placeMemo("스케줄 메모 테스트")
                .arrivalTime("21:00:00")
                .build();

        // PlanBudgetDto
        PlanBudgetDto planBudgetDto = PlanBudgetDto.builder().build();

        // when
        // Plan 저장
        Plan newPlan = Plan.builder()
                .userId(planDto.getUserId())
                .planName(planDto.getPlanName())
                .theme(planDto.getTheme())
                .startDate(planDto.getStartDate())
                .endDate(planDto.getEndDate())
                .scope(planDto.isScope())
                .numberOfMembers(planDto.getNumberOfMembers())
                .numberOfLikes(INITIAL_VALUE)
                .numberOfScraps(INITIAL_VALUE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        Plan insertedPlan = planRepository.save(newPlan);

        // PlanSchedule 저장
        PlanSchedule newPlanSchedule = PlanSchedule.builder()
                .planId(insertedPlan.getId())
                .scheduleDate(planScheduleDto.getScheduleDate())
                .placeCategory(planScheduleDto.getPlaceCategory())
                .placeName(planScheduleDto.getPlaceName())
                .placeMemo(planScheduleDto.getPlaceMemo())
                .arrivalTime(LocalTime.parse(planScheduleDto.getArrivalTime(), DateTimeFormatter.ofPattern("HH:mm:ss")))
                .x(planScheduleDto.getX())
                .y(planScheduleDto.getY())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        PlanSchedule insertedPlanSchedule = planScheduleRepository.save(newPlanSchedule);

        // PlanBudget 저장
        PlanBudget newPlanBudget = PlanBudget.builder()
                .scheduleId(insertedPlanSchedule.getId())
                .budgetCategory(planBudgetDto.getBudgetCategory())
                .cost(planBudgetDto.getCost())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        PlanBudget insertedPlanBudget = planBudgetRepository.save(newPlanBudget);

        // then
        Assertions.assertEquals(newPlan, insertedPlan);
        Assertions.assertEquals(newPlanSchedule, insertedPlanSchedule);
        Assertions.assertEquals(newPlanBudget, insertedPlanBudget);
    }
}
