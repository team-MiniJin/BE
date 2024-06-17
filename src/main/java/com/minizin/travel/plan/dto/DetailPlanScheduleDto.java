package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.PlanSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
@JsonPropertyOrder({"id", "scheduleDays", "scheduleDate", "placeCategory", "placeName", "region", "placeMemo", "arrivalTime", "detailPlanBudgetDtoList", "x", "y", "placeAddr"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetailPlanScheduleDto {

    @JsonProperty("schedule_id")
    private Long id;

    @Setter
    private int scheduleDays;

    private LocalDate scheduleDate;

    private String placeName;

    private String region;

    private String placeMemo;

    private String arrivalTime;

    private Double x;
    private Double y;

    private String placeAddr;

    private String placeCategory;

    @Setter
    @JsonProperty("budgets")
    List<DetailPlanBudgetDto> detailPlanBudgetDtoList;

    public static DetailPlanScheduleDto toDto(PlanSchedule planSchedule) {
        return DetailPlanScheduleDto.builder()
                .id(planSchedule.getId())
                .scheduleDate(planSchedule.getScheduleDate())
                .placeName(planSchedule.getPlaceName())
                .region(planSchedule.getRegion())
                .placeMemo(planSchedule.getPlaceMemo())
                .arrivalTime(planSchedule.getArrivalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .x(planSchedule.getX())
                .y(planSchedule.getY())
                .placeAddr(planSchedule.getPlaceAddr())
                .placeCategory(planSchedule.getPlaceCategory())
                .build();
    }
}
