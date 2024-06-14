package com.minizin.travel.plan.repository;

import com.minizin.travel.plan.entity.PlanBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanBudgetRepository extends JpaRepository<PlanBudget, Long> {

    List<PlanBudget> findAllByScheduleId(Long scheduleId);
}
