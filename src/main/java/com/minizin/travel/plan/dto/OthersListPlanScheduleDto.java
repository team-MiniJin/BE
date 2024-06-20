package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.PlanSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@JsonPropertyOrder({"id", "scheduleDate", "placeName", "region", "arrivalTime", "x", "y"
        , "placeAddr", "placeCategory"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OthersListPlanScheduleDto {

    @JsonProperty("schedule_id")
    private Long id;

    private LocalDate scheduleDate;

    private String placeName;

    private String region;

    private String arrivalTime;

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
                .arrivalTime(planSchedule.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .x(planSchedule.getX())
                .y(planSchedule.getY())
                .placeAddr(planSchedule.getPlaceAddr())
                .placeCategory(planSchedule.getPlaceCategory())
                .build();
    }
}
