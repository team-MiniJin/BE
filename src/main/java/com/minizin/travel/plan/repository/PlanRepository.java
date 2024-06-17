package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByIdLessThanAndUserIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    // #39 2024.06.10 다가오는 여행 일정 조회 //
    List<Plan> findTop6ByUserIdAndStartDateAfterOrderByStartDateAsc(Long userId, LocalDate today);

    // #48 2024.06.10 다른 사람 여행 일정 조회 START //

    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotOrderByIdDesc(Long userId, Pageable pageable);
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //


    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByNumberOfScrapsAscIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotOrderByNumberOfScrapsAscIdDesc(Long userId, Pageable pageable);
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    List<Plan> findTop20ByStartDateBetweenOrderByNumberOfScrapsAsc(LocalDate startDate, LocalDate endDate);
}
