package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"userId", "planName", "theme", "startDate", "endDate", "scope", "numberOfMembers", "schedules"})
public class PlanDto {

    private Long userId;

    // #87 Request 예외/에러 처리
    @Size(min = 2, max = 60, message = "'여행 일정 이름'은 2 ~ 60자여야 합니다.")
    private String planName;

    private String theme;

    private String startDate;

    private String endDate;

    private boolean scope;

    // #87 Request 예외/에러 처리
    @Range(min = 1, max = 300, message = "'여행 인원'은 1 ~ 20명이어야 합니다.")
    private int numberOfMembers;

    @JsonProperty("schedules")
    private List<PlanScheduleDto> planScheduleDtos;

}