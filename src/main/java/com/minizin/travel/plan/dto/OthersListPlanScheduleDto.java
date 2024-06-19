package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minizin.travel.plan.entity.PlanSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class OthersListPlanScheduleDto {

    @JsonProperty("schedule_id")
    private Long id;

    private LocalDate scheduleDate;

    private String placeName;

    private String region;

    private LocalTime arrivalTime;

    private Double x;
    private Double y;

    private String placeAddr;

    private String placeCategory;

    public static OthersListPlanScheduleDto toDto(PlanSchedule planSchedule) {
        return OthersListPlanScheduleDto.builder()
                .id(planSchedule.getId())
                .scheduleDate(planSchedule.getScheduleDate())
                .placeName(planSchedule.getPlaceName())
                .region(planSchedule.getRegion())
                .arrivalTime(planSchedule.getArrivalTime())
                .x(planSchedule.getX())
                .y(planSchedule.getY())
                .placeAddr(planSchedule.getPlaceAddr())
                .placeCategory(planSchedule.getPlaceCategory())
                .build();
    }
}
