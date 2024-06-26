package com.minizin.travel.plan.service;

import com.minizin.travel.plan.dto.*;
import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import com.minizin.travel.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final UserRepository userRepository;

    // #48 2024.06.10 다른 사람 여행 일정 조회 START //
    public ResponseOthersPlanDto selectOthersListPlan(
            Long lastPlanId, String region, String theme, String search) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        List<Plan> planList = findOtherPlanByLastPlanIdCheckExistCursor(region, theme, search, lastPlanId, page);
        List<OthersListPlanDto> othersListPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            othersListPlanDtoList.add(selectOthersPlanAndSchedule(plan));
        }

        if (findOtherPlanByLastPlanIdCheckExistCursor(region, theme, search, planId, page).isEmpty()) {

            planId = null;
        }

        return ResponseOthersPlanDto.builder()
                .data(othersListPlanDtoList)
                .nextCursor(planId)
                .build();
    }

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    public ResponseOthersPlanDto selectOthersListPlanScraps(
            Long lastPlanId, String region, String theme, String search) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        List<Plan> planList = findOtherPlanScrapsByLastPlanIdCheckExistCursor(region, theme, search, lastPlanId, page);
        List<OthersListPlanDto> othersListPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            othersListPlanDtoList.add(selectOthersPlanAndSchedule(plan));
        }

        if (findOtherPlanScrapsByLastPlanIdCheckExistCursor(region, theme, search, planId, page).isEmpty()) {
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
        othersListPlanDto.setUserNickname(userRepository.findById(plan.getUserId()).get().getNickname());
        othersListPlanDto.setRegionList(planService.duplicateRegionList(regionList));
        othersListPlanDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));
        othersListPlanDto.setOthersListPlanScheduleDtoList(othersListPlanScheduleDtoList);

        return othersListPlanDto;
    }

    private List<Plan> findOtherPlanByLastPlanIdCheckExistCursor(String region, String theme, String search, Long lastPlanId, Pageable page) {

        if (StringUtils.isEmpty(region)) {
            region = null;
        }
        if (StringUtils.isEmpty(theme)) {
            theme = null;
        }
        if (StringUtils.isEmpty(search)) {
            search = null;
        }

        return lastPlanId == 0 ? planRepository.findSearchAndThemeAndRegionOrderByIdDesc(region, theme, search, page)
                : planRepository.findLessThanSearchAndThemeAndRegionOrderByIdDesc(lastPlanId, region, theme, search, page);
    }
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    private List<Plan> findOtherPlanScrapsByLastPlanIdCheckExistCursor(String region, String theme, String search, Long lastPlanId, Pageable page) {

        if (StringUtils.isEmpty(region)) {
            region = null;
        }
        if (StringUtils.isEmpty(theme)) {
            theme = null;
        }
        if (StringUtils.isEmpty(search)) {
            search = null;
        }
        return lastPlanId == 0? planRepository.findSearchAndThemeAndRegionOrderByNumberOfScrapsDescIdDesc(region, theme, search, page)
                : planRepository.findLessThanSearchAndThemeAndRegionOrderByNumberOfScrapsDescIdDesc(lastPlanId, region, theme, search, page);
    }
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    // #106 2024.06.16 금주 인기 여행 일정 조회(북마크순) START //
    public List<PopWeekPlanDto> selectPopularPlansWeek() {

        List<PopWeekPlanDto> popWeekPlanDtoList = new ArrayList<>();

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(6);
        List<Plan> planList = planRepository.findTop20ByCreatedAtBetweenOrderByNumberOfScrapsDescIdDesc(startDate, endDate);

        for (Plan plan : planList) {
            PopWeekPlanDto newPopWeekPlanDto = PopWeekPlanDto.toDto(plan);
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
            newPopWeekPlanDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));
            newPopWeekPlanDto.setUserNickname(userRepository.findById(plan.getUserId()).get().getNickname());
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

        if (!plan.isScope()) {
            return DetailPlanDto.notAuth(planId, "비공개 plan 입니다.");
        }

        DetailPlanDto detailPlanDto = planService.findListOfPlanScheduleDtoAndPlanBudgetDto(plan);
        detailPlanDto.setUserNickname(userRepository.findById(plan.getUserId()).get().getNickname());

        return detailPlanDto;
    }
    // #129 다른 사람의 여행 일정 상세 보기 END //
}
