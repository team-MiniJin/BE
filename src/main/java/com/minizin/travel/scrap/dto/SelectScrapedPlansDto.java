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
@JsonPropertyOrder({"id", "planId", "userNickname", "planName", "theme", "startDate", "endDate",
        "planBudget", "scope", "numberOfMembers", "numberOfScraps"})
public class SelectScrapedPlansDto {

    @Setter
    @JsonProperty("scrap_id")
    private Long id;

    private Long planId;

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

    private int numberOfScraps;

    public static SelectScrapedPlansDto toDto(Plan plan) {
        return SelectScrapedPlansDto.builder()
                .planId(plan.getId())
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
