package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.PlanSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanScheduleRepository extends JpaRepository<PlanSchedule, Long> {

    List<PlanSchedule> findAllByPlanId(Long planId);
}
