package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePlanDto {
    boolean success;
    String message;

    @JsonProperty("plan_id")
    Long planId;

    @JsonProperty("number_of_likes")
    int numberOfLikes;

    @JsonProperty("number_of_scrapse")
    int numberOfScraps;

    @JsonProperty("created_at")
    String createAt;

    @JsonProperty("updated_at")
    String updatedAt;
}
