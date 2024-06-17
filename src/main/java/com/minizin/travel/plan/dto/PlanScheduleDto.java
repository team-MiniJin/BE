package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlanScheduleDto {

    private String scheduleDate;

    private String placeCategory;

    @Size(min = 1, max = 20, message = "'장소 이름'의 길이는 1 ~ 20자입니다.")
    private String placeName;

    private String region;

    @Size(max = 60, message = "'메모'의 길이는 최대 60자입니다.")
    private String placeMemo;

    private String arrivalTime;

    @JsonProperty("budgets")
    private List<PlanBudgetDto> planBudgetDtos;

    private Double x;
    private Double y;

    // #29 2024.06.02 내 여행 일정 조회
    private String placeAddr;
}

