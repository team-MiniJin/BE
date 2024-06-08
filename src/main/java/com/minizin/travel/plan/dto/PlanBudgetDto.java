package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanBudgetDto {

    @JsonProperty("budget_id")
    private Long id;

    @JsonProperty("budget_category")
    private String budgetCategory;

    private int cost;

    @JsonProperty("budget_memo")
    private String budgetMemo;

}
