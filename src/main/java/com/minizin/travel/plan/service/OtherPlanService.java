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

        // 테스트
        Long userId = 2L;

        List<Plan> planList = findOtherPlanByLastPlanIdCheckExistCursor(userId, region, theme, lastPlanId, page);
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

        // 테스트
        Long userId = 2L;

        List<Plan> planList = findOtherPlanScrapsByLastPlanIdCheckExistCursor(userId, region, theme, lastPlanId, page);
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

    private List<Plan> findOtherPlanByLastPlanIdCheckExistCursor(Long userId, String region, String theme, Long lastPlanId, Pageable page) {

        if (region.isEmpty() && theme.isEmpty()) {
            // region / theme 둘다 미존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndUserIdNotOrderByIdDesc(userId, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByIdDesc(lastPlanId, userId, page);
        } else if (region.isEmpty()) {
            // theme 만 존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndUserIdNotAndThemeOrderByIdDesc(userId, theme, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndUserIdNotAndThemeOrderByIdDesc(lastPlanId, userId, theme, page);
        } else if (theme.isEmpty()) {
            // region 만 존재
            return lastPlanId == 0 ? planRepository.findScopeIsTrueAndUserIdNotAndRegionOrderByIdDesc(userId, region, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueAndUserIdNotAndRegionOrderByIdDesc(lastPlanId, userId, region, DEFAULT_PAGE_SIZE);
        } else {
            // region / theme 둘다 존재

            // region / theme 둘다 미존재로 에러 방지용
            return lastPlanId == 0 ? planRepository.findScopeIsTrueAndUserIdNotThemeAndRegionOrderByIdDesc(userId, region, theme, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueAndUserIdNotThemeAndRegionOrderByIdDesc(lastPlanId, userId, region, theme, DEFAULT_PAGE_SIZE);
        }
    }
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    private List<Plan> findOtherPlanScrapsByLastPlanIdCheckExistCursor(Long userId, String region, String theme, Long lastPlanId, Pageable page) {

        if (region.isEmpty() && theme.isEmpty()) {
            // region / theme 둘다 미존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndUserIdNotOrderByNumberOfScrapsDescIdDesc(userId, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByNumberOfScrapsDescIdDesc(lastPlanId, userId, page);
        } else if (region.isEmpty()) {
            // theme 만 존재
            return lastPlanId == 0 ? planRepository.findAllByScopeIsTrueAndUserIdNotAndThemeOrderByNumberOfScrapsDescIdDesc(userId, theme, page)
                    : planRepository.findByIdLessThanAndScopeIsTrueAndUserIdNotAndThemeOrderByNumberOfScrapsDescIdDesc(lastPlanId, userId, theme, page);
        } else if (theme.isEmpty()) {
            // region 만 존재

            return lastPlanId == 0 ? planRepository.findScopeIsTrueAndUserIdNotAndRegionOrderByNumberOfScrapsDescIdDesc(userId, region, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueAndUserIdNotAndRegionOrderByNumberOfScrapsDescIdDesc(lastPlanId, userId, region, DEFAULT_PAGE_SIZE);
        } else {
            // region / theme 둘다 존재

            // region / theme 둘다 미존재로 에러 방지용
            return lastPlanId == 0 ? planRepository.findScopeIsTrueAndUserIdNotThemeAndRegionOrderByNumberOfScrapsDescIdDesc(userId, region, theme, DEFAULT_PAGE_SIZE)
                    : planRepository.findLessThanAndScopeIsTrueAndUserIdNotThemeAndRegionOrderByNumberOfScrapsDescIdDesc(lastPlanId, userId, region, theme, DEFAULT_PAGE_SIZE);
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

        Plan plan = planRepository.findById(planId).get();

        DetailPlanDto detailPlanDto = planService.findListOfPlanScheduleDtoAndPlanBudgetDto(plan);
        detailPlanDto.setUserNickname("유저 닉네임 HARD CORDING");

        return detailPlanDto;
    }
    // #129 다른 사람의 여행 일정 상세 보기 END //
}
