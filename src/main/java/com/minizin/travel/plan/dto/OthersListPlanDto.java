package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "userId", "userNickname", "planName", "theme", "startDate", "endDate"
        , "planBudget", "scope", "numberOfMembers", "numberOfScraps", "regionList", "othersListPlanScheduleDtoList"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OthersListPlanDto {

    @JsonProperty("plan_id")
    private Long id;

    private Long userId;

    @Setter
    private String userNickname;

    private String planName;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private int planBudget;

    private boolean scope;

    private int numberOfMembers;

    private  int numberOfScraps;

    @Setter
    List<String> regionList;

    @Setter
    @JsonProperty("schedules")
    List<OthersListPlanScheduleDto> othersListPlanScheduleDtoList;

    public static OthersListPlanDto toDto(Plan plan) {
        return OthersListPlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .planName(plan.getPlanName())
                .theme(plan.getTheme())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .scope(plan.isScope())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfScraps(plan.getNumberOfScraps())
                .build();
    }
}
