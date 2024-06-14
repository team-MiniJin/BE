package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate", "planBudget", "scope", "numberOfMembers",
        "numberOfLikes", "numberOfScraps", "waypoints", "detailPlanScheduleDtoList"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetailPlanDto {

    @JsonProperty("plan_id")
    private Long id;

    private Long userId;

    private String planName;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private int planBudget;

    private boolean scope;

    private int numberOfMembers;

    private int numberOfLikes;

    private int numberOfScraps;

    @Setter
    private List<String> waypoints;

    @Setter
    @JsonProperty("schedules")
    private List<DetailPlanScheduleDto> detailPlanScheduleDtoList;

    public static DetailPlanDto toDto(Plan plan) {
        return DetailPlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .planName(plan.getPlanName())
                .theme(plan.getTheme())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .scope(plan.isScope())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfLikes(plan.getNumberOfLikes())
                .numberOfScraps(plan.getNumberOfScraps())
                .build();
    }

}