package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlanScheduleDto {

    private LocalDate scheduleDate;

    private String placeCategory;

    private String placeName;

    private String region;

    private String placeMemo;

    private String arrivalTime;

    @JsonProperty("budget")
    private List<PlanBudgetDto> planBudgetDtos;

    private Double x;
    private Double y;

}

