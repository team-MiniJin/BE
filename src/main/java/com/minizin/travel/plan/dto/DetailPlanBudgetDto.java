package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.PlanBudget;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonPropertyOrder({"id", "budgetCategory", "cost", "budgetMemo"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetailPlanBudgetDto {

    @JsonProperty("budget_id")
    private Long id;

    private String budgetCategory;

    private Integer cost;

    public static DetailPlanBudgetDto toDto(PlanBudget planBudget) {
        return DetailPlanBudgetDto.builder()
                .id(planBudget.getId())
                .budgetCategory(planBudget.getBudgetCategory())
                .cost(planBudget.getCost())
                .build();
    }
}
