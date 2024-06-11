package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.Plan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByIdLessThanAndUserIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    List<Plan> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

}
