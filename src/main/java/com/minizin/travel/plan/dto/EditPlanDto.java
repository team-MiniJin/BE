package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate"
        , "scope", "numberOfMembers", "numberOfScraps", "schedules"})
public class EditPlanDto {

    private Long userId;

    private String planName;

    private String theme;

    private String startDate;

    private String endDate;

    private boolean scope;

    private int numberOfMembers;

    private int numberOfScraps;

    @JsonProperty("schedules")
    private List<PlanScheduleDto> planScheduleDtos;

}
