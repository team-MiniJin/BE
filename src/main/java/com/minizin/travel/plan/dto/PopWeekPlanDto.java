package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "planName","userId", "userNickname", "theme", "startDate"
        ,"endDate", "startDate", "endDate", "planBudget", "numberOfMembers", "numberOfScraps"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PopWeekPlanDto {

    @JsonProperty("plan_id")
    private Long id;

    private String planName;

    private Long userId;

    @Setter
    private String userNickname;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private int planBudget;

    private int numberOfMembers;

    private int numberOfScraps;

    public static PopWeekPlanDto toDto(Plan plan) {

        return PopWeekPlanDto.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .userId(plan.getUserId())
                .theme(plan.getTheme())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfScraps(plan.getNumberOfScraps())
                .build();
    }

}
