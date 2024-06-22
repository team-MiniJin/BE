package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCheckScrapedPlanDto {

    private boolean success;

    private String message;

    @JsonPropertyOrder("scrap_id")
    private Long scrapId;
}
