package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate", "scope", "numberOfMembers", "numberOfLikes", "numberOfScraps", "waypoints", "scheduleDtos"})
public class PlanDto {

    private Long userId;

    private String planName;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean scope;

    private int numberOfMembers;

    //private int numberOfLikes;

    //private int numberOfScraps;

    //private List<String> waypoints;

    @JsonProperty("schedule")
    private List<PlanScheduleDto> planScheduleDtos;

}