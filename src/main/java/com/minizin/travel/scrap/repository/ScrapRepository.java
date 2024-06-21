package com.minizin.travel.scrap.repository;

import com.minizin.travel.scrap.entity.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // #49 스크랩 생성 : 이미 스크랩한 일정인지 확인 //
    boolean existsByPlanIdAndUserId(Long planId, Long userId);

    // #50 2024.06.12 스크랩 조회 : 해당 회원이 스크랩한 일정이 있는지 확인 //
    boolean existsByUserId(Long userId);

    // #50 2024.06.12 스크랩 조회 : 해당 회원이 스크랩한 일정 모두 조회 //
    List<Scrap> findAllByUserIdOrderByIdDesc(Long userId, Pageable page);

    List<Scrap> findByIdLessThanAndUserIdOrderByIdDesc(Long cursorId, Long userId, Pageable page);

    // #51 스크랩 삭제 전 삭제할 스크랩이 있는지 조회
    Optional<Scrap> findByPlanIdAndUserId(Long planId, Long userId);
}
