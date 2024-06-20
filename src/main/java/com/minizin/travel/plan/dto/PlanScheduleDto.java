package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.PlanSchedule;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlanScheduleDto {

    @Setter
    private String scheduleDate; // 직렬화, 역직렬화 문제로 수정

    private String placeCategory;

    @Size(min = 1, max = 40, message = "'장소 이름'의 길이는 1 ~ 40자입니다.")
    private String placeName;

    private String region;

    @Size(max = 100, message = "'메모'의 길이는 최대 100자입니다.")
    private String placeMemo;

    private String arrivalTime;

    @JsonProperty("budgets")
    @Setter
    private List<PlanBudgetDto> planBudgetDtos;

    private Double x;
    private Double y;

    // #29 2024.06.02 내 여행 일정 조회
    private String placeAddr;

    public static PlanScheduleDto toDto(PlanSchedule planSchedule) {
        return PlanScheduleDto.builder()
                .scheduleDate(String.valueOf(planSchedule.getScheduleDate()))
                .placeCategory(planSchedule.getPlaceCategory())
                .placeName(planSchedule.getPlaceName())
                .region(planSchedule.getRegion())
                .placeMemo(planSchedule.getPlaceMemo())
                .arrivalTime(String.valueOf(planSchedule.getArrivalTime()))
                .x(planSchedule.getX())
                .y(planSchedule.getY())
                .placeAddr(planSchedule.getPlaceAddr())
                .build();
    }
}

