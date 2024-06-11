package com.minizin.travel.plan.service;

import com.minizin.travel.plan.dto.*;
import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanBudget;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanBudgetRepository;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final PlanBudgetRepository planBudgetRepository;

    final int INITIAL_VALUE = 0;
    final int DEFAULT_PAGE_SIZE = 6; // #29

    // #28 2024.05.30 내 여행 일정 생성하기 START //
    public ResponsePlanDto createPlan(PlanDto planDto) {

        Plan newPlan = planRepository.save(Plan.builder()
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
                .build());

        Long planId = newPlan.getId();

        for (PlanScheduleDto planScheduleDto : planDto.getPlanScheduleDtos()) {
            Long scheduleId = createPlanSchedule(planScheduleDto, planId).getId();

            for (PlanBudgetDto planBudgetDto : planScheduleDto.getPlanBudgetDtos()) {
                createPlanBudget(planBudgetDto, scheduleId);
            }
        }

        ResponsePlanDto newResponsePlanDto = ResponsePlanDto.builder()
                .success(true)
                .message("일정을 생성하였습니다.")
                .planId(planId)
                .numberOfLikes(newPlan.getNumberOfLikes())
                .numberOfScraps(newPlan.getNumberOfScraps())
                .createAt(newPlan.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(newPlan.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return newResponsePlanDto;
    }

    public PlanSchedule createPlanSchedule(PlanScheduleDto planScheduleDto, Long planId) {

        return planScheduleRepository.save(PlanSchedule.builder()
                .planId(planId)
                .scheduleDate(planScheduleDto.getScheduleDate())
                .placeCategory(planScheduleDto.getPlaceCategory())
                .placeName(planScheduleDto.getPlaceName())
                .region(planScheduleDto.getRegion())
                .placeMemo(planScheduleDto.getPlaceMemo())
                .arrivalTime(LocalTime.parse(planScheduleDto.getArrivalTime(), DateTimeFormatter.ofPattern("HH:mm:ss")))
                .x(planScheduleDto.getX())
                .y(planScheduleDto.getY())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());
    }

    public PlanBudget createPlanBudget(PlanBudgetDto planBudgetDto, Long scheduleId) {

        return planBudgetRepository.save(PlanBudget.builder()
                .scheduleId(scheduleId)
                .budgetCategory(planBudgetDto.getBudgetCategory())
                .cost(planBudgetDto.getCost())
                .budgetMemo(planBudgetDto.getBudgetMemo())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());
    }
    // #28 2024.05.30 내 여행 일정 생성하기 END //
}
