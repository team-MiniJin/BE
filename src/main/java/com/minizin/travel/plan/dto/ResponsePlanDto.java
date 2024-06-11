package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponsePlanDto {

    boolean success;
    String message;

    Long planId;

    int numberOfLikes;

    int numberOfScraps;

    String createAt;

    String updatedAt;
}
