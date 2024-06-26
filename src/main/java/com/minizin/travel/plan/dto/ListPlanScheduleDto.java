package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.PlanSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

// 2024.06.05 내 여행 일정 조회 //
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "scheduleDate", "placeName", "arrivalTime", "x", "y"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListPlanScheduleDto {

    @JsonProperty("schedule_id")
    private Long id;

    private LocalDate scheduleDate;

    private String placeName;

    private LocalTime arrivalTime;

    private Double x;
    private Double y;

    private String placeAddr;

    private String placeCategory;

    public static ListPlanScheduleDto toDto(PlanSchedule planSchedule) {
        return ListPlanScheduleDto.builder()
                .id(planSchedule.getId())
                .scheduleDate(planSchedule.getScheduleDate())
                .placeName(planSchedule.getPlaceName())
                .arrivalTime(planSchedule.getArrivalTime())
                .x(planSchedule.getX())
                .y(planSchedule.getY())
                .placeAddr(planSchedule.getPlaceAddr())
                .placeCategory(planSchedule.getPlaceCategory())
                .build();
    }
}

