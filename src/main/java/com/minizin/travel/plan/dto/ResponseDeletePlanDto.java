package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseDeletePlanDto {

    private boolean success;

    private String message;

    private Long planId;

}
