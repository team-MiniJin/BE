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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtherPlanService {

    private final PlanService planService;
    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;

    final int DEFAULT_PAGE_SIZE = 6; // #29
    private final PlanBudgetRepository planBudgetRepository;

    // #48 2024.06.10 다른 사람 여행 일정 조회 START //
    public ResponseOthersPlanDto selectOthersListPlan(Long lastPlanId, String region, String theme) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        List<Plan> planList = findOtherPlanByLastPlanIdCheckExistCursor(region, theme, lastPlanId, page);
        List<OthersListPlanDto> othersListPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            othersListPlanDtoList.add(selectOthersPlanAndSchedule(plan));
        }

        if (!planRepository.existsByIdLessThan(planId)) {
            planId = null;
        }

        return ResponseOthersPlanDto.builder()
                .data(othersListPlanDtoList)
                .nextCursor(planId)
                .build();
    }

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    public ResponseOthersPlanDto selectOthersListPlanScraps(Long lastPlanId, String region, String theme) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        List<Plan> planList = findOtherPlanScrapsByLastPlanIdCheckExistCursor(region, theme, lastPlanId, page);
        List<OthersListPlanDto> othersListPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            othersListPlanDtoList.add(selectOthersPlanAndSchedule(plan));
        }

        if (!planRepository.existsByIdLessThan(planId)) {
            planId = null;
        }

        return ResponseOthersPlanDto.builder()
                .data(othersListPlanDtoList)
                .nextCursor(planId)
                .build();
    }
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

/*
    private List<OthersListPlanDto> selectOthersListPlan(List<Plan> planList) {

        List<OthersListPlanDto> othersListPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            othersListPlanDtoList.add(selectOthersPlanAndSchedule(plan));
        }

        return othersListPlanDtoList;
    }
 */

    private OthersListPlanDto selectOthersPlanAndSchedule(Plan plan) {

        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
        List<OthersListPlanScheduleDto> othersListPlanScheduleDtoList = new ArrayList<>();
        List<String> regionList = new ArrayList<>();

        for (PlanSchedule planSchedule : planScheduleList) {
            othersListPlanScheduleDtoList.add(OthersListPlanScheduleDto.toDto(planSchedule));
            regionList.add(planSchedule.getRegion());
        }

        OthersListPlanDto othersListPlanDto = OthersListPlanDto.toDto(plan);
        othersListPlanDto.setRegionList(planService.duplicateRegionList(regionList));
        othersListPlanDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));
        othersListPlanDto.setOthersListPlanScheduleDtoList(othersListPlanScheduleDtoList);

        return othersListPlanDto;
    }

    private List<Plan> findOtherPlanByLastPlanIdCheckExistCursor(String region, String theme, Long lastPlanId, Pageable page) {

        if (region.isEmpty() && theme.isEmpty()) {
            // region / theme 둘다 미존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueOrderByIdDesc(page)
                    : planRepository.findByIdLessThanAndScopeIsTrueOrderByIdDesc(lastPlanId, page);
        } else if (region.isEmpty()) {
            // theme 만 존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndThemeOrderByIdDesc(theme, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndThemeOrderByIdDesc(lastPlanId, theme, page);
        } else if (theme.isEmpty()) {
            // region 만 존재
            return lastPlanId == 0 ? planRepository.findScopeIsTrueNotAndRegionOrderByIdDesc(region, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueNotAndRegionOrderByIdDesc(lastPlanId, region, DEFAULT_PAGE_SIZE);
        } else {
            // region / theme 둘다 존재

            // region / theme 둘다 미존재로 에러 방지용
            return lastPlanId == 0 ? planRepository.findScopeIsTrueNotThemeAndRegionOrderByIdDesc(region, theme, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueNotThemeAndRegionOrderByIdDesc(lastPlanId, region, theme, DEFAULT_PAGE_SIZE);
        }
    }
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    private List<Plan> findOtherPlanScrapsByLastPlanIdCheckExistCursor(String region, String theme, Long lastPlanId, Pageable page) {

        if (region.isEmpty() && theme.isEmpty()) {
            // region / theme 둘다 미존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueOrderByNumberOfScrapsDescIdDesc(page)
                    : planRepository.findByIdLessThanAndScopeIsTrueOrderByNumberOfScrapsDescIdDesc(lastPlanId, page);
        } else if (region.isEmpty()) {
            // theme 만 존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndThemeOrderByNumberOfScrapsDescIdDesc(theme, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndThemeOrderByNumberOfScrapsDescIdDesc(lastPlanId, theme, page);
        } else if (theme.isEmpty()) {
            // region 만 존재

            return lastPlanId == 0 ? planRepository.findScopeIsTrueAndRegionOrderByNumberOfScrapsDescIdDesc(region, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueAndRegionOrderByNumberOfScrapsDescIdDesc(lastPlanId, region, DEFAULT_PAGE_SIZE);
        } else {
            // region / theme 둘다 존재

            // region / theme 둘다 미존재로 에러 방지용
            return lastPlanId == 0 ? planRepository.findScopeIsTrueThemeAndRegionOrderByNumberOfScrapsDescIdDesc(region, theme, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueThemeAndRegionOrderByNumberOfScrapsDescIdDesc(lastPlanId, region, theme, DEFAULT_PAGE_SIZE);
        }
    }
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    // #106 2024.06.16 금주 인기 여행 일정 조회(북마크순) START //
    public List<PopWeekPlanDto> selectPopularPlansWeek() {

        List<PopWeekPlanDto> popWeekPlanDtoList = new ArrayList<>();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        List<Plan> planList = planRepository.findTop20ByStartDateBetweenOrderByNumberOfScrapsDescIdDesc(startDate, endDate);

        for (Plan plan : planList) {
            PopWeekPlanDto newPopWeekPlanDto = PopWeekPlanDto.toDto(plan);
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
            newPopWeekPlanDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));
            // 유저 닉네임 추가 newPopWeekPlanDto.setUserNickname();
            popWeekPlanDtoList.add(newPopWeekPlanDto);
        }

        return popWeekPlanDtoList;
    }
    // #106 2024.06.16 금주 인기 여행 일정 조회(북마크순) END //

    // #129 다른 사람의 여행 일정 상세 보기 START //
    public DetailPlanDto selectOtherDetailPlan(Long planId) {

        if (!planRepository.existsById(planId)) {
            return DetailPlanDto.existsNot(planId);
        }

        Plan plan = planRepository.findById(planId).get();

        DetailPlanDto detailPlanDto = planService.findListOfPlanScheduleDtoAndPlanBudgetDto(plan);
        detailPlanDto.setUserNickname("유저 닉네임 HARD CORDING");

        return detailPlanDto;
    }
    // #129 다른 사람의 여행 일정 상세 보기 END //
}
