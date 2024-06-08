package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanScheduleDto {

    @JsonProperty("schedule_id")
    private Long id;

    @JsonProperty("schedule_date")
    private LocalDate scheduleDate;

    @JsonProperty("place_category")
    private String placeCategory;

    @JsonProperty("place_name")
    private String placeName;

    private String region;

    @JsonProperty("place_memo")
    private String placeMemo;

    @JsonProperty("arrival_time")
    private String arrivalTime;

    @JsonProperty("budget")
    private List<PlanBudgetDto> planBudgetDtos;

    private Double x;
    private Double y;

}

