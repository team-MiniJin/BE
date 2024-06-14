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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                .placeAddr(planScheduleDto.getPlaceAddr()) // #29 2024.06.02 내 여행 일정 조회
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
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());
    }
    // #28 2024.05.30 내 여행 일정 생성하기 END //

    // #29 2024.06.02 내 여행 일정 조회 START //
    public ResponseListPlanDto selectListPlan(Long cursorId) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        // 테스트
        Long userId = 1L;

        List<Plan> planList = findAllByCursorIdCheckExistCursor(userId, cursorId, page);
        List<ListPlanDto> listPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId);
            List<ListPlanScheduleDto> listPlanScheduleDtoList = new ArrayList<>();
            List<String> waypoints = new ArrayList<>();
            int totalBudget = calculateTotalPlanBudget(planScheduleList); // #44 여행 일정 예산 추가

            for (PlanSchedule planSchedule : planScheduleList) {
                ListPlanScheduleDto newResponseScheduleDto = ListPlanScheduleDto.toDto(planSchedule);
                listPlanScheduleDtoList.add(newResponseScheduleDto);
                waypoints.add(planSchedule.getRegion());
            }
            ListPlanDto newPlanDto = ListPlanDto.toDto(plan);
            newPlanDto.setPlanBudget(totalBudget); // #44 여행 일정 예산 추가
            newPlanDto.setListPlanScheduleDtoList(listPlanScheduleDtoList);
            newPlanDto.setWaypoints(duplicateWaypoints(waypoints));
            listPlanDtoList.add(newPlanDto);
        }

        return ResponseListPlanDto.builder()
                .data(listPlanDtoList)
                .nextCursor(planId)
                .build();
    }

    private List<String> duplicateWaypoints(List<String> waypoints) {

        if (waypoints.size() <= 1) {
            return waypoints;
        }

        List<String> newWaypoints = new ArrayList<>();
        newWaypoints.add(waypoints.get(0));

        for (int i = 1; i < waypoints.size(); i++) {
            if (waypoints.get(i - 1).equals(waypoints.get(i))) {
                continue;
            }
            newWaypoints.add(waypoints.get(i));
        }

        return newWaypoints;
    }

    private List<Plan> findAllByCursorIdCheckExistCursor(Long userId, Long cursorId, Pageable page) {

        return cursorId == 0 ? planRepository.findAllByUserIdOrderByIdDesc(userId, page)
                : planRepository.findByIdLessThanAndUserIdOrderByIdDesc(cursorId, userId, page);

    }
    // #29 2024.06.02 내 여행 일정 조회 END //

    // #44 2024.06.12 여행 일정 예산 계산하기 START //
    private int calculateTotalPlanBudget(List<PlanSchedule> planScheduleList) {

        int totalPlanBudget = 0;

        for (PlanSchedule planSchedule : planScheduleList) {
            List<PlanBudget> planBudgetList = planBudgetRepository.findAllByScheduleId(planSchedule.getId());

            for (PlanBudget planBudget : planBudgetList) {
                totalPlanBudget += planBudget.getCost();
            }
        }

        return totalPlanBudget;
    }
    // #44 2024.06.12 여행 일정 예산 계산하기 END //

    // #38 2024.06.08 내 여행 일정 상세 보기 START //
    public DetailPlanDto selectDetailPlan(Long planId) {

        Plan plan = planRepository.findById(planId).get();

        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId); // DB를 가져온 list
        List<DetailPlanScheduleDto> detailPlanScheduleDtoList = new ArrayList<>(); // 객체를 가져온 list
        List<String> waypoints = new ArrayList<>();
        int days = 0;

        for (PlanSchedule planSchedule : planScheduleList) {
            List<PlanBudget> planBudgetList = planBudgetRepository.findAllByScheduleId(planSchedule.getId());
            List<DetailPlanBudgetDto> detailPlanBudgetDtoList = new ArrayList<>();

            for (PlanBudget planBudget : planBudgetList) {
                detailPlanBudgetDtoList.add(DetailPlanBudgetDto.toDto(planBudget));
            }

            DetailPlanScheduleDto detailPlanScheduleDto = DetailPlanScheduleDto.toDto(planSchedule);
            waypoints.add(planSchedule.getRegion());
            detailPlanScheduleDto.setScheduleDays(++days);
            detailPlanScheduleDto.setDetailPlanBudgetDtoList(detailPlanBudgetDtoList);
            detailPlanScheduleDtoList.add(detailPlanScheduleDto);
        }

        DetailPlanDto detailPlanDto = DetailPlanDto.toDto(plan);
        detailPlanDto.setPlanBudget(calculateTotalPlanBudget(planScheduleList));
        detailPlanDto.setWaypoints(duplicateWaypoints(waypoints));
        detailPlanDto.setDetailPlanScheduleDtoList(detailPlanScheduleDtoList);

        return detailPlanDto;
    }
    // #38 2024.06.08 내 여행 일정 상세 보기 END //

    // #39 2024.06.10 다가오는 여행 일정 조회 START //
    public List<UpcomingPlanDto> selectUpcomingPlan() {

        // 테스트
        Long userId = 1L;

        List<UpcomingPlanDto> upcomingPlanDtoList = new ArrayList<>();
        List<Plan> planList = planRepository.findTop6ByUserIdAndStartDateAfterOrderByStartDateAsc(userId, LocalDate.now());

        for (Plan plan : planList) {
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());

            UpcomingPlanDto upcomingPlanDto = UpcomingPlanDto.toDto(plan);
            upcomingPlanDto.setPlanBudget(calculateTotalPlanBudget(planScheduleList));
            upcomingPlanDtoList.add(upcomingPlanDto);
        }

        return upcomingPlanDtoList;
    }
    // #39 2024.06.10 다가오는 여행 일정 조회 END //
}
