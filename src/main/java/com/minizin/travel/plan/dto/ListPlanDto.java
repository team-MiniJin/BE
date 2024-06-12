package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate", "planBudget", "scope", "numberOfMembers", "numberOfLikes", "numberOfScraps", "waypoints", "responseScheduleListDtos"})
public class ListPlanDto {

    @JsonProperty("plan_id")
    private Long id;

    private Long userId;

    private String planName;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private int planBudget; // #44 여행 일정 예산 추가

    private boolean scope;

    private int numberOfMembers;

    private int numberOfLikes;

    private int numberOfScraps;

    @Setter
    private List<String> waypoints;

    @JsonProperty("schedule")
    @Setter
    private List<ListPlanScheduleDto> listPlanScheduleDtoList;

    public static ListPlanDto toDto(Plan plan) {
        return ListPlanDto.builder()
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