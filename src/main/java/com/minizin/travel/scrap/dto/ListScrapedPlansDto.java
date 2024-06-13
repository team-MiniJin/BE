package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "userNickname", "planName", "theme", "startDate", "planBudget", "scope", "numberOfMembers", "numberOfLikes", "numberOfScraps"})
public class ListScrapedPlansDto {

    @JsonProperty("plan_id")
    private Long id;

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

    private int numberOfLikes;

    private int numberOfScraps;

    public static ListScrapedPlansDto toDto(Plan plan) {
        return ListScrapedPlansDto.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .theme(plan.getTheme())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .scope(plan.isScope())
                .numberOfLikes(plan.getNumberOfLikes())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfScraps(plan.getNumberOfScraps())
                .build();
    }
}
