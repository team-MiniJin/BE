/*
package com.minizin.travel.like.service;

import com.minizin.travel.like.dto.ResponseCreateLikePlanDto;
import com.minizin.travel.like.dto.ResponseDeleteLikePlanDto;
import com.minizin.travel.like.dto.ResponseSelectLikedPlansDto;
import com.minizin.travel.like.dto.SelectLikedPlansDto;
import com.minizin.travel.like.entity.Likes;
import com.minizin.travel.like.repository.LikeRepository;
import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import com.minizin.travel.plan.repository.PlanRepository;
import com.minizin.travel.plan.repository.PlanScheduleRepository;
import com.minizin.travel.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PlanRepository planRepository;
    private final PlanScheduleRepository planScheduleRepository;
    private final PlanService planService;

    final int DEFAULT_PAGE_SIZE = 6;

    // #52 2024.06.13 좋아요 생성 START //
    public ResponseCreateLikePlanDto createLikePlan(Long planId) {

        // 테스트
        Long userId = 1L;

        if (!planRepository.existsById(planId)) {
            throw new RuntimeException("유효하지 않은 plan id");
        }

        if (likeRepository.existsByPlanIdAndUserId(planId, userId)) {
            throw new RuntimeException("이미 좋아요를 누른 plan");
        }

        Plan plan = planRepository.findById(planId).get();
        plan.setNumberOfLikes(plan.getNumberOfLikes() + 1);

        return ResponseCreateLikePlanDto.toDto(likeRepository.save(Likes.builder()
                        .userId(userId)
                        .planId(planId)
                        .createdAt(LocalDateTime.now())
                        .build()));
    }
    // #52 2024.06.13 좋아요 생성 END //

    // #53 2024.06.13 좋아요 조회 START //
    public ResponseSelectLikedPlansDto selectListLikedPlans(Long cursorId) {

        Pageable page = PageRequest.of(0, DEFAULT_PAGE_SIZE);

        // 테스트
        Long userId = 1L;

        if (!likeRepository.existsByUserId(userId)) {
            throw new RuntimeException("좋아요한 plan이 없습니다.");
        }

        List<Likes> likeList = findAllByCursorIdCheckExistCursor(userId, cursorId, page);
        List<SelectLikedPlansDto> listLikedPlansDtoList = new ArrayList<>();
        Long likeId = 0L;
        for (Likes like : likeList) {
            likeId = like.getId();
            Plan plan = planRepository.findById(like.getPlanId()).get();

            SelectLikedPlansDto newSelectLikedPlansDto = SelectLikedPlansDto.toDto(plan);
            newSelectLikedPlansDto.setId(likeId);
            // 닉네임 추가 필요 newLikedPlansDto.setUserNickname

            // 예산 계산
            List<PlanSchedule> planScheduleList = planScheduleRepository.findAllByPlanId(plan.getId());
            newSelectLikedPlansDto.setPlanBudget(planService.calculateTotalPlanBudget(planScheduleList));

            listLikedPlansDtoList.add(newSelectLikedPlansDto);
        }

        return ResponseSelectLikedPlansDto.builder()
                .data(listLikedPlansDtoList)
                .cursorId(likeId)
                .build();
    }

    private List<Likes> findAllByCursorIdCheckExistCursor(Long userId, Long cursorId, Pageable page) {

        return cursorId == 0 ? likeRepository.findAllByUserIdOrderByIdDesc(userId, page)
                : likeRepository.findByIdLessThanAndUserIdOrderByIdDesc(cursorId, userId, page);
    }
    // #53 2024.06.13 좋아요 조회 END //

    // #54 2024.06.14 좋아요 삭제 START //
    public ResponseDeleteLikePlanDto deleteLikedPlan(Long likeId) {

        if (!likeRepository.existsById(likeId)) {

            return ResponseDeleteLikePlanDto.builder()
                    .success(false)
                    .message("존재하지 않는 like_id 입니다.")
                    .likeId(likeId)
                    .build();
        }

        Long planId = likeRepository.findById(likeId).get().getPlanId();
        Plan plan = planRepository.findById(planId).get();
        plan.setNumberOfLikes(plan.getNumberOfLikes() - 1);

        likeRepository.deleteById(likeId);

        return ResponseDeleteLikePlanDto.builder()
                .success(true)
                .message("Like Deleted Successfully")
                .likeId(likeId)
                .build();
    }
    // #54 2024.06.14 좋아요 삭제 END //

}
*/
