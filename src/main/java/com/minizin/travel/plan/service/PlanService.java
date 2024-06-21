package com.minizin.travel.plan.service;

import com.minizin.travel.plan.dto.*;
import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanBudget;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanBudgetRepository;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final PlanBudgetRepository planBudgetRepository;
    private final UserRepository userRepository;

    final int INITIAL_VALUE = 0;
    final int DEFAULT_PAGE_SIZE = 6; // #29

    // #28 2024.05.30 내 여행 일정 생성하기 START //
    public ResponsePlanDto createPlan(PlanDto planDto, PrincipalDetails user)
            throws BadRequestException {

        // user 정보 확인
        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        // #87 Request 예외/에러 처리 START
        // 여행 일자 최소 1일 ~ 최대 60일 START //
        LocalDate startDate = LocalDate.parse(planDto.getStartDate());
        LocalDate endDate = LocalDate.parse(planDto.getEndDate());
        int planDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        if (planDays < 0 || planDays > 60) {
            System.out.println("여행의 날짜는 최소 1일 ~ 최대 60일이어야 합니다.");
            throw new BadRequestException();
        }

        // 일정당 장소 최소 1개
        String curDate = "";
        int scheduleCnt = 0;
        for (PlanScheduleDto planScheduleDto : planDto.getPlanScheduleDtos()) {
            if (curDate.equals(planScheduleDto.getScheduleDate())) {
                scheduleCnt++;
                if (scheduleCnt > 40) {
                    System.out.println("일정의 개수는 하루당 40개를 넘을 수 없습니다.");
                    throw new BadRequestException();
                }
            } else {
                curDate = planScheduleDto.getScheduleDate();
                scheduleCnt = 1;
            }
        }

        // 예산 ~ 100,000,000원
        int totalPlanBudget = 0;
        for (PlanScheduleDto planScheduleDto : planDto.getPlanScheduleDtos()) {

            if (planScheduleDto.getPlanBudgetDtos() != null) { // #87 Request 예외/에러 처리
                for (PlanBudgetDto planBudgetDto : planScheduleDto.getPlanBudgetDtos()) {
                    totalPlanBudget += planBudgetDto.getCost();
                }
            }
        }
        if (totalPlanBudget > 100000000) {
            throw new BadRequestException();
        }

        // schedule 날짜가 유효하지 않은 경우
        for (PlanScheduleDto planScheduleDto : planDto.getPlanScheduleDtos()) {
            LocalDate scheduleDate = LocalDate.parse(planScheduleDto.getScheduleDate());
            if (scheduleDate.isBefore(startDate)
             || scheduleDate.isAfter(endDate)) {

                return ResponsePlanDto.fail(
                        ResponsePlanDto.Data.builder()
                                    .startDate(planDto.getStartDate())
                                    .endDate(planDto.getEndDate())
                                    .scheduleDate(planScheduleDto.getScheduleDate())
                                    .build());
            }
        }
        // #87 Request 예외/에러 처리 END //

        Plan newPlan = planRepository.save(Plan.builder()
                .userId(userId)
                .planName(planDto.getPlanName())
                .theme(planDto.getTheme())
                .startDate(startDate)
                .endDate(endDate)
                .scope(planDto.isScope())
                .numberOfMembers(planDto.getNumberOfMembers())
                .numberOfScraps(INITIAL_VALUE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());

        Long planId = newPlan.getId();

        for (PlanScheduleDto planScheduleDto : planDto.getPlanScheduleDtos()) {
                Long scheduleId = createPlanSchedule(planScheduleDto, planId).getId();

            if (planScheduleDto.getPlanBudgetDtos() != null) { // #87 Request 예외/에러 처리
                for (PlanBudgetDto planBudgetDto : planScheduleDto.getPlanBudgetDtos()) {
                    createPlanBudget(planBudgetDto, scheduleId);
                }
            }
        }

        return ResponsePlanDto.success(newPlan); // #87 Request 예외/에러 처리 : Response success 코드 정리

    }

    public PlanSchedule createPlanSchedule(PlanScheduleDto planScheduleDto, Long planId) {

        return planScheduleRepository.save(PlanSchedule.builder()
                .planId(planId)
                .scheduleDate(LocalDate.parse(planScheduleDto.getScheduleDate()))
                .placeCategory(planScheduleDto.getPlaceCategory())
                .placeName(planScheduleDto.getPlaceName())
                .placeAddr(planScheduleDto.getPlaceAddr()) // #29 2024.06.02 내 여행 일정 조회
                .region(planScheduleDto.getRegion())
                .placeMemo(planScheduleDto.getPlaceMemo())
                .arrivalTime(LocalTime.parse(planScheduleDto.getArrivalTime(), DateTimeFormatter.ofPattern("HH:mm")))
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
    public ResponseSelectListPlanDto selectListPlan(Long lastPlanId, PrincipalDetails user) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        List<Plan> planList = findAllByLastPlanIdCheckExistCursor(userId, lastPlanId, page);
        List<SelectListPlanDto> listPlanDtoList = new ArrayList<>();
        Long planId = 0L;

        for (Plan plan : planList) {
            planId = plan.getId();
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId);
            List<SelectListPlanScheduleDto> listPlanScheduleDtoList = new ArrayList<>();
            List<String> regionList = new ArrayList<>();
            int totalBudget = calculateTotalPlanBudget(planScheduleList); // #44 여행 일정 예산 추가

            for (PlanSchedule planSchedule : planScheduleList) {
                listPlanScheduleDtoList.add(SelectListPlanScheduleDto.toDto(planSchedule));
                regionList.add(planSchedule.getRegion());
            }

            SelectListPlanDto selectListPlanDto = SelectListPlanDto.toDto(plan);
            selectListPlanDto.setPlanBudget(totalBudget); // #44 여행 일정 예산 추가
            selectListPlanDto.setListPlanScheduleDtoList(listPlanScheduleDtoList);
            selectListPlanDto.setRegionList(duplicateRegionList(regionList));
            listPlanDtoList.add(selectListPlanDto);
        }

        if (!planRepository.existsByIdLessThanAndUserId(planId, userId)) {
            planId = null;
        }

        return ResponseSelectListPlanDto.builder()
                .data(listPlanDtoList)
                .nextCursor(planId)
                .build();
    }

    public List<String> duplicateRegionList(List<String> regionList) {

        if (regionList.size() <= 1) {
            return regionList;
        }

        List<String> newRegionList = new ArrayList<>();
        newRegionList.add(regionList.get(0));

        // [ 서울 -> 서울 -> 부산 ] 인 경우 [ 서울 -> 부산 ] 으로 중복 제거
        for (int i = 1; i < regionList.size(); i++) {
            if (regionList.get(i - 1).equals(regionList.get(i))) {
                continue;
            }
            newRegionList.add(regionList.get(i));
        }

        return newRegionList;
    }

    private List<Plan> findAllByLastPlanIdCheckExistCursor(Long userId, Long lastPlanId, Pageable page) {

        return lastPlanId == 0 ? planRepository.findAllByUserIdOrderByIdDesc(userId, page)
                : planRepository.findByIdLessThanAndUserIdOrderByIdDesc(lastPlanId, userId, page);

    }
    // #29 2024.06.02 내 여행 일정 조회 END //

    // #44 2024.06.12 여행 일정 예산 계산하기 START //
    public int calculateTotalPlanBudget(List<PlanSchedule> planScheduleList) {

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

    // #32 2024.06.07 내 여행 일정 수정 START //
    @Transactional
    public ResponseEditPlanDto updatePlan(Long planId, EditPlanDto editPlanDto, PrincipalDetails user) {

        if (!planRepository.existsById(planId)) {
            return ResponseEditPlanDto.builder()
                    .success(false)
                    .message("요청하신 plan 은 존재하지 않습니다.")
                    .planId(planId)
                    .build();
        }

        /* 추후 PUT요청에 id값이 포함되면 변경 예정 */

        // 기존 PlanSchedule 및 PlanBudget 삭제
        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId);
        for (PlanSchedule planSchedule : planScheduleList) {
            planBudgetRepository.deleteByScheduleId(planSchedule.getId());
        }
        planScheduleRepository.deleteByPlanId(planId);

        // plan id 로 plan DB 찾아오기
        // plan DB 에 저장
        Plan plan = planRepository.findById(planId).get();

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();
        if (!plan.getUserId().equals(userId)) {
            return ResponseEditPlanDto.builder()
                    .success(false)
                    .message("로그인한 사용자의 id가 아닙니다.")
                    .planId(planId)
                    .build();
        }

        Plan newPlan = planRepository.save(Plan.builder()
                .id(planId)
                .userId(plan.getUserId())
                .planName(editPlanDto.getPlanName())
                .theme(editPlanDto.getTheme())
                .startDate(LocalDate.parse(editPlanDto.getStartDate()))
                .endDate(LocalDate.parse(editPlanDto.getEndDate()))
                .scope(editPlanDto.isScope())
                .numberOfMembers(editPlanDto.getNumberOfMembers())
                .numberOfScraps(plan.getNumberOfScraps())
                .createdAt(plan.getCreatedAt())
                .modifiedAt(LocalDateTime.now())
                .build());

        for (PlanScheduleDto planScheduleDto : editPlanDto.getPlanScheduleDtos()) {
            Long scheduleId = createPlanSchedule(planScheduleDto, planId).getId();

            for (PlanBudgetDto planBudgetDto : planScheduleDto.getPlanBudgetDtos()) {
                createPlanBudget(planBudgetDto, scheduleId);
            }
        }

        return ResponseEditPlanDto.builder()
                .success(true)
                .message("일정을 수정하였습니다.")
                .planId(planId)
                .createdAt(newPlan.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(newPlan.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .numberOfScraps(newPlan.getNumberOfScraps())
                .build();
    }
    // #32 2024.06.07 내 여행 일정 수정 END //

    // #38 2024.06.08 내 여행 일정 상세 보기 START //
    public DetailPlanDto selectDetailPlan(Long planId, PrincipalDetails user) {

        if (!planRepository.existsById(planId)) {
            return DetailPlanDto.existsNot(planId);
        }

        Plan plan = planRepository.findById(planId).get();
        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();
        if (!plan.getUserId().equals(userId)) {
            return DetailPlanDto.notAuth(planId, "로그인한 사용자의 plan 이 아닙니다.");
        }

        return findListOfPlanScheduleDtoAndPlanBudgetDto(plan);
    }

    public DetailPlanDto findListOfPlanScheduleDtoAndPlanBudgetDto(Plan plan) {

        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId()); // DB를 가져온 list
        List<DetailPlanScheduleDto> detailPlanScheduleDtoList = new ArrayList<>(); // 객체를 가져온 list
        List<String> regionList = new ArrayList<>();
        int days = 0;

        for (PlanSchedule planSchedule : planScheduleList) {
            List<PlanBudget> planBudgetList = planBudgetRepository.findAllByScheduleId(planSchedule.getId());
            List<DetailPlanBudgetDto> detailPlanBudgetDtoList = new ArrayList<>();

            for (PlanBudget planBudget : planBudgetList) {
                detailPlanBudgetDtoList.add(DetailPlanBudgetDto.toDto(planBudget));
            }

            DetailPlanScheduleDto detailPlanScheduleDto = DetailPlanScheduleDto.toDto(planSchedule);
            regionList.add(planSchedule.getRegion());
            detailPlanScheduleDto.setScheduleDays((int) ChronoUnit.DAYS.between(plan.getStartDate(), detailPlanScheduleDto.getScheduleDate()) + 1);
            detailPlanScheduleDto.setDetailPlanBudgetDtoList(detailPlanBudgetDtoList);
            detailPlanScheduleDtoList.add(detailPlanScheduleDto);
        }

        DetailPlanDto detailPlanDto = DetailPlanDto.toDto(plan);
        detailPlanDto.setPlanBudget(calculateTotalPlanBudget(planScheduleList));
        detailPlanDto.setRegionList(duplicateRegionList(regionList));
        detailPlanDto.setDetailPlanScheduleDtoList(detailPlanScheduleDtoList);

        return detailPlanDto;
    }
    // #38 2024.06.08 내 여행 일정 상세 보기 END //

    // #47 2024.06.13 내 여행 일정 삭제 START //
    @Transactional
    public ResponseDeletePlanDto deletePlan(Long planId, PrincipalDetails user) {

        if (!planRepository.existsById(planId)) {
            return ResponseDeletePlanDto.builder()
                    .success(false)
                    .message("여행 일정 id가 유효하지 않습니다.")
                    .planId(planId)
                    .build();
        }

        Plan plan = planRepository.findById(planId).get();
        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();
        if (!plan.getUserId().equals(userId)) {
            return ResponseDeletePlanDto.builder()
                    .success(false)
                    .message("로그인한 사용자의 plan 이 아닙니다.")
                    .planId(planId)
                    .build();
        }

        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId);
        for (PlanSchedule planSchedule : planScheduleList) {
            planBudgetRepository.deleteByScheduleId(planSchedule.getId());
        }
        planScheduleRepository.deleteByPlanId(planId);

        planRepository.deleteById(planId);

        return ResponseDeletePlanDto.builder()
                .success(true)
                .message("일정을 삭제하였습니다.")
                .planId(planId)
                .build();
    }
    // #47 2024.06.13 내 여행 일정 삭제 END //

    // #39 2024.06.10 다가오는 여행 일정 조회 START //
    public List<UpcomingPlanDto> selectUpcomingPlan(PrincipalDetails user) {

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        List<UpcomingPlanDto> upcomingPlanDtoList = new ArrayList<>();
        List<Plan> planList = planRepository.findTop6ByUserIdAndStartDateGreaterThanEqualOrderByStartDateAscIdDesc(userId, LocalDate.now());

        for (Plan plan : planList) {
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());

            UpcomingPlanDto upcomingPlanDto = UpcomingPlanDto.toDto(plan);
            upcomingPlanDto.setPlanBudget(calculateTotalPlanBudget(planScheduleList));
            upcomingPlanDtoList.add(upcomingPlanDto);
        }

        return upcomingPlanDtoList;
    }
    // #39 2024.06.10 다가오는 여행 일정 조회 END //

    // #107 2024.06.20 일정 복사하기 START //
    public ResponsePlanDto copyAndCreatePlan(Long planId, PrincipalDetails user) {

        if (!planRepository.existsById(planId)) {
            return ResponsePlanDto.existsNot(planId);
        }

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        Plan plan = planRepository.findById(planId).get();

        int planDays = (int) ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate());
        LocalDate endDate = LocalDate.now().plusDays(planDays);
        Plan newPlan = planRepository.save(Plan.builder()
                .userId(userId)
                .planName(plan.getPlanName())
                .theme(plan.getTheme())
                .startDate(LocalDate.now())
                .endDate(endDate)
                .scope(plan.isScope())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfScraps(INITIAL_VALUE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());

        // 기존 스케줄 찾기
        List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(planId);
        planDays = (int) ChronoUnit.DAYS.between(plan.getStartDate(), LocalDate.now());

        for (PlanSchedule planSchedule : planScheduleList) {
            PlanScheduleDto planScheduleDto = PlanScheduleDto.toDto(planSchedule);
            planScheduleDto.setScheduleDate(String.valueOf(LocalDate.parse(planScheduleDto.getScheduleDate()).plusDays(planDays)));
            Long scheduleId = createPlanSchedule(planScheduleDto, newPlan.getId()).getId();

            // 기존 예산 찾기
            List<PlanBudget> planBudgetList = planBudgetRepository.findAllByScheduleId(planSchedule.getId());
            // 새 예산 넣기
            for (PlanBudget planBudget : planBudgetList) {
                createPlanBudget(PlanBudgetDto.toDto(planBudget), scheduleId);
            }
        }

        return ResponsePlanDto.copySuccess(newPlan); // #87 Request 예외/에러 처리 : Response success 코드 정리

    }
    // #107 2024.06.20 일정 복사하기 END //
}
