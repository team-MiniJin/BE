package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate", "scope", "numberOfMembers", "numberOfLikes", "numberOfScraps", "waypoints", "scheduleDtos"})
public class PlanDto {

    @JsonProperty("plan_id")
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("plan_name")
    private String planName;

    private String theme;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private boolean scope;

    @JsonProperty("number_of_members")
    private int numberOfMembers;

    @JsonProperty("number_of_likes")
    private int numberOfLikes;

    @JsonProperty("number_of_scraps")
    private int numberOfScraps;

    private List<String> waypoints;

    @JsonProperty("schedule")
    private List<PlanScheduleDto> planScheduleDtos;

}