package com.minizin.travel.scrap.service;

import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import com.minizin.travel.plan.service.PlanService;
import com.minizin.travel.scrap.dto.*;
import com.minizin.travel.scrap.entity.Scrap;
import com.minizin.travel.scrap.repository.ScrapRepository;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.UserErrorCode;
import com.minizin.travel.user.domain.exception.CustomUserException;
import com.minizin.travel.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final UserRepository userRepository;
    private final PlanService planService;

    final int DEFAULT_PAGE_SIZE = 6;

    // #49 스크랩 생성 START //
    public ResponseCreateScrapPlanDto createScrap(Long planId,
                                                  PrincipalDetails user) {

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        // 해당 plan 이 DB에 저장되었는지 확인
        if (!planRepository.existsById(planId)) {
            return ResponseCreateScrapPlanDto.fail(planId, "요청하신 plan 은 존재하지 않습니다.");
        }

        // 해당 plan 을 이미 저장하지 않았는지 확인
        if (scrapRepository.existsByPlanIdAndUserId(planId, userId)) {
            return ResponseCreateScrapPlanDto.fail(planId, "이미 북마크한 plan 입니다.");
        }

        Plan plan = planRepository.findById(planId).get();

        /*
        if (plan.getUserId().equals(userId)) {
            return ResponseCreateScrapPlanDto.fail(planId, "본인의 plan은 북마크할 수 없습니다.");
        }
        */

        plan.setNumberOfScraps(plan.getNumberOfScraps() + 1);

        return ResponseCreateScrapPlanDto.toDto(scrapRepository.save(Scrap.builder()
                .userId(userId)
                .planId(planId)
                .createdAt(LocalDateTime.now())
                .build()));
    }
    // #49 스크랩 생성 END //

    // #50 스크랩 조회 START //
    public ResponseSelectScrapedPlansDto selectListScrapedPlans(Long cursorId, PrincipalDetails user) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        if (!scrapRepository.existsByUserId(userId)) {
            return ResponseSelectScrapedPlansDto.fail("북마크한 plan 이 없습니다.");
        }

        List<Scrap> scrapList = findAllByCursorIdCheckExistCursor(userId, cursorId, page);
        List<SelectScrapedPlansDto> scrapedPlansDtoList = new ArrayList<>();
        Long scrapId = 0L;
        for (Scrap scrap : scrapList) {
            scrapId = scrap.getId();
            Plan plan = planRepository.findById(scrap.getPlanId()).get();

            SelectScrapedPlansDto newSelectScrapedPlanDto = SelectScrapedPlansDto.toDto(plan);
            newSelectScrapedPlanDto.setId(scrapId);
            newSelectScrapedPlanDto.setUserNickname(userRepository.findById(plan.getUserId()).get().getNickname());

            // 예산 계산
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
            newSelectScrapedPlanDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));

            scrapedPlansDtoList.add(newSelectScrapedPlanDto);
        }

        if (!findAllByCursorIdCheckExistCursor(userId, scrapId, page).isEmpty()) {
            scrapId = null;
        }

        return ResponseSelectScrapedPlansDto.builder()
                .data(scrapedPlansDtoList)
                .cursorId(scrapId)
                .build();
    }

    private List<Scrap> findAllByCursorIdCheckExistCursor(Long userId, Long cursorId, Pageable page) {

        return cursorId == 0 ? scrapRepository.findAllByUserIdOrderByIdDesc(userId, page)
                : scrapRepository.findByIdLessThanAndUserIdOrderByIdDesc(cursorId, userId, page);
    }
    // #50 스크랩 조회 END //

    // #51 스크랩 삭제 START //
    @Transactional
    public ResponseDeleteScrapedPlanDto deleteScrapedPlan(Long planId, PrincipalDetails user) {

        Long userId = userRepository.findByUsername(user.getUsername()).get().getId();

        if (!scrapRepository.existsByPlanIdAndUserId(planId, userId)) {

            return ResponseDeleteScrapedPlanDto.builder()
                    .success(false)
                    .message("존재하지 않는 scrap 입니다.")
                    .build();
        }

        Scrap scrap = scrapRepository.findByPlanIdAndUserId(planId, userId).get();

        Plan plan = planRepository.findById(scrap.getPlanId()).get();
        plan.setNumberOfScraps(plan.getNumberOfScraps() - 1);

        scrapRepository.deleteById(scrap.getId());

        return ResponseDeleteScrapedPlanDto.builder()
                .success(true)
                .message("Scrap Deleted Successfully")
                .scrapId(scrap.getId())
                .build();
    }
    // #51 스크랩 삭제 END //

    public ResponseCheckScrapedPlanDto checkScrapedPlan(Long planId, PrincipalDetails user) {

        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomUserException(UserErrorCode.USER_NOT_FOUND));

        if (!scrapRepository.existsByPlanIdAndUserId(planId, userEntity.getId())) {

            return ResponseCheckScrapedPlanDto.builder()
                    .success(false)
                    .message("존재하지 않는 scrap 입니다.")
                    .build();
        }

        return ResponseCheckScrapedPlanDto.builder()
                .success(true)
                .message("존재하는 scrap 입니다.")
                .build();
    }
}
