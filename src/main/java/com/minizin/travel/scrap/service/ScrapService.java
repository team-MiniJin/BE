package com.minizin.travel.scrap.service;

import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import com.minizin.travel.plan.service.PlanService;
import com.minizin.travel.scrap.dto.ResponseCreateScrapPlanDto;
import com.minizin.travel.scrap.dto.ResponseSelectScrapedPlansDto;
import com.minizin.travel.scrap.dto.SelectScrapedPlansDto;
import com.minizin.travel.scrap.entity.Scrap;
import com.minizin.travel.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final PlanService planSerivce;

    final int DEFAULT_PAGE_SIZE = 6;

    // #49 스크랩 생성 START //
    public ResponseCreateScrapPlanDto createScrap(Long planId) {

        // 테스트
        Long userId = 1L;

        // 해당 plan 이 DB에 저장되었는지 확인
        if (!planRepository.existsById(planId)) {
            throw new RuntimeException("유효하지 않은 plan id");
        }

        // 해당 plan 을 이미 저장하지 않았는지 확인
        if (scrapRepository.existsByPlanIdAndUserId(planId, userId)) {
            throw new RuntimeException("이미 저장된 plan");
        }

        Plan plan = planRepository.findById(planId).get();
        plan.setNumberOfScraps(plan.getNumberOfScraps() + 1);

        return ResponseCreateScrapPlanDto.toDto(scrapRepository.save(Scrap.builder()
                .userId(userId)
                .planId(planId)
                .createdAt(LocalDateTime.now())
                .build()));
    }
    // #49 스크랩 생성 END //

    // #50 스크랩 조회 START //
    public ResponseSelectScrapedPlansDto selectListScrapedPlans(Long cursorId) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        // 테스트
        Long userId = 1L;

        if (!scrapRepository.existsByUserId(userId)) {
            throw new RuntimeException("스크랩된 plan이 없습니다.");
        }

        List<Scrap> scrapList = findAllByCursorIdCheckExistCursor(userId, cursorId, page);
        List<SelectScrapedPlansDto> scrapedPlansDtoList = new ArrayList<>();
        Long scrapId = 0L;
        for (Scrap scrap : scrapList) {
            scrapId = scrap.getId();
            Plan plan = planRepository.findById(scrap.getPlanId()).get();

            SelectScrapedPlansDto newListScrpaedPlanDto = SelectScrapedPlansDto.toDto(plan);
            newListScrpaedPlanDto.setId(scrapId);
            // 닉네임 추가 필요

            // 예산 계산
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
            newListScrpaedPlanDto.setPlanBudget(planSerivce.calculateTotalPlanBudget(planScheduleList));

            scrapedPlansDtoList.add(newListScrpaedPlanDto);
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
}
