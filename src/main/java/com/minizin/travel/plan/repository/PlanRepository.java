package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.Plan;
import com.minizin.travel.plan.entity.PlanSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByIdLessThanAndUserIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    boolean existsByIdLessThan(Long id);

    // #39 2024.06.10 다가오는 여행 일정 조회 //
    List<Plan> findTop6ByUserIdAndStartDateAfterOrderByStartDateAsc(Long userId, LocalDate today);

    // #48 2024.06.10 다른 사람 여행 일정 조회 START //
    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotOrderByIdDesc(Long userId, Pageable pageable);

    // 다른 사람 여행 일정 조회(테마)
    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotAndThemeOrderByIdDesc(Long id, Long userId, String theme, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotAndThemeOrderByIdDesc(Long userId, String theme, Pageable pageable);

    // 다른 사람 여행 일정 조회(지역)
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "and p.scope is true order by p.id desc limit :limit")
    List<Plan> findLessThanAndScopeIsTrueAndUserIdNotAndRegionOrderByIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("userId") Long userId, @Param("region") String region, @Param("limit") int limit);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId\n" +
            "and p.scope is true and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "order by p.id desc limit :limit")
    List<Plan> findScopeIsTrueAndUserIdNotAndRegionOrderByIdDesc(@Param("userId") Long userId, @Param("region") String region, @Param("limit") int limit);

    // 다른 사람 여행 일정 조회(테마+지역)
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "and p.theme = :theme and p.scope is true order by p.id desc limit :limit")
    List<Plan> findLessThanAndScopeIsTrueAndUserIdNotThemeAndRegionOrderByIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("userId") Long userId, @Param("region") String region, @Param("theme") String theme, @Param("limit") int limit);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "and p.theme = :theme and p.scope is true order by p.id desc limit :limit")
    List<Plan> findScopeIsTrueAndUserIdNotThemeAndRegionOrderByIdDesc(
            @Param("userId") Long userId, @Param("region") String region, @Param("theme") String theme, @Param("limit") int limit);
    // #48 2024.06.10 다른 사람 여행 일정 조회 END //

    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) START //
    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotOrderByNumberOfScrapsDescIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotOrderByNumberOfScrapsDescIdDesc(Long userId, Pageable pageable);

    // 다른 사람 여행 일정 조회(북마크순)(테마)
    List<Plan> findByIdLessThanAndScopeIsTrueAndUserIdNotAndThemeOrderByNumberOfScrapsDescIdDesc(Long id, Long userId, String theme, Pageable pageable);

    List<Plan> findAllByScopeIsTrueAndUserIdNotAndThemeOrderByNumberOfScrapsDescIdDesc(Long userId, String theme, Pageable pageable);

    // 다른 사람 여행 일정 조회(북마크순)(지역)
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId\n" +
            "and p.scope is true and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "order by p.numberOfScraps desc, p.id desc limit :limit")
    List<Plan> findLessThanAndScopeIsTrueAndUserIdNotAndRegionOrderByNumberOfScrapsDescIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("userId") Long userId, @Param("region") String region, @Param("limit") int limit);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId\n" +
            "and p.scope is true and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "order by p.numberOfScraps desc, p.id desc limit :limit")
    List<Plan> findScopeIsTrueAndUserIdNotAndRegionOrderByNumberOfScrapsDescIdDesc(@Param("userId") Long userId, @Param("region") String region, @Param("limit") int limit);

    // 다른 사람 여행 일정 조회(테마+지역)
    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.id < :lastPlanId and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "and p.theme = :theme and p.scope is true order by p.numberOfScraps desc, p.id desc limit :limit")
    List<Plan> findLessThanAndScopeIsTrueAndUserIdNotThemeAndRegionOrderByNumberOfScrapsDescIdDesc(
            @Param("lastPlanId") Long lastPlanId, @Param("userId") Long userId, @Param("region") String region, @Param("theme") String theme, @Param("limit") int limit);

    @Query("select DISTINCT p from Plan p, PlanSchedule s\n" +
            "where p.id = s.planId and p.userId <> :userId and s.placeAddr like %:region%\n" +
            "and p.theme = :theme and p.scope is true order by p.numberOfScraps desc, p.id desc limit :limit")
    List<Plan> findScopeIsTrueAndUserIdNotThemeAndRegionOrderByNumberOfScrapsDescIdDesc(
            @Param("userId") Long userId, @Param("region") String region, @Param("theme") String theme, @Param("limit") int limit);
    // #58 2024.06.12 다른 사람 여행 일정 조회(북마크순) END //

    // #129 금주 인기 여행
    List<Plan> findTop20ByStartDateBetweenOrderByNumberOfScrapsDescIdDesc(LocalDate startDate, LocalDate endDate);
}
