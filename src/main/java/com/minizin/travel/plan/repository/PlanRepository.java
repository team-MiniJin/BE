package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    /*List<Plan> findPageNextPage(Long cursorId, Long userId, Pageable pageable);

    List<Plan> findPagesByUserIdOrderByIdDesc(Long userId, Pageable pageable);*/

    // 03
    List<Plan> findByIdLessThanAndUserIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    // 02
    // List<Plan> findByUserIdLessThanOrderByIdDesc(Long userId, Long id, Pageable pageable);

    // 01
    //List<Plan> findByUserIdLessThanOrderByPlanIdDesc(Long userId, Long id, Pageable pageable);

    List<Plan> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

}
