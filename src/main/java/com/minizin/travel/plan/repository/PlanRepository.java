package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByIdLessThanAndUserIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    boolean existsByIdLessThanAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    // #39 2024.06.10 다가오는 여행 일정 조회 //
    List<Plan> findTop6ByUserIdAndStartDateGreaterThanEqualOrderByStartDateAscIdDesc(Long userId, LocalDate today);

    // #48 2024.06.10 다른 사람 여행 일정 조회 START //
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId\n" +
            "and (:region IS NULL OR s.placeAddr like %:region%)\n" +
            "and (:theme IS NULL OR p.theme = :theme )\n" +
            "and (:search IS NULL OR (p.planName like %:search%\n" +
            "OR s.placeAddr like %:search%\n" +
            "OR s.placeName like %:search%))\n" +
            "and p.scope is true order by p.id desc")
    List<Plan> findLessThanSearchAndThemeAndRegionOrderByIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("region") String region, @Param("theme") String theme, @Param("search") String search, Pageable pageable);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId\n" +
            "and (:region IS NULL OR s.placeAddr like %:region%)\n" +
            "and (:theme IS NULL OR p.theme = :theme )\n" +
            "and (:search IS NULL OR (p.planName like %:search%\n" +
            "OR s.placeAddr like %:search%\n" +
            "OR s.placeName like %:search%))\n" +
            "and p.scope is true order by p.id desc")
    List<Plan> findSearchAndThemeAndRegionOrderByIdDesc(
            @Param("region") String region, @Param("theme") String theme, @Param("search") String search, Pageable pageable);
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId\n" +
            "and (:region IS NULL OR s.placeAddr like %:region%)\n" +
            "and (:theme IS NULL OR p.theme = :theme )\n" +
            "and (:search IS NULL OR (p.planName like %:search%\n" +
            "OR s.placeAddr like %:search%\n" +
            "OR s.placeName like %:search%))\n" +
            "and p.scope is true order by p.numberOfScraps desc, p.id desc")
    List<Plan> findLessThanSearchAndThemeAndRegionOrderByNumberOfScrapsDescIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("region") String region, @Param("theme") String theme, @Param("search") String search, Pageable pageable);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId\n" +
            "and (:region IS NULL OR s.placeAddr like %:region%)\n" +
            "and (:theme IS NULL OR p.theme = :theme )\n" +
            "and (:search IS NULL OR (p.planName like %:search%\n" +
            "OR s.placeAddr like %:search%\n" +
            "OR s.placeName like %:search%))\n" +
            "and p.scope is true order by p.numberOfScraps desc, p.id desc")
    List<Plan> findSearchAndThemeAndRegionOrderByNumberOfScrapsDescIdDesc(
            @Param("region") String region, @Param("theme") String theme, @Param("search") String search, Pageable pageable);
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    // #129 금주 인기 여행
    //List<Plan> findTop20ByStartDateBetweenOrderByNumberOfScrapsDescIdDesc(LocalDate startDate, LocalDate endDate);

    List<Plan> findTop20ByCreatedAtBetweenOrderByNumberOfScrapsDescIdDesc(LocalDateTime startDate, LocalDateTime endDate);

}
