package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDeleteScrapedPlanDto {

    private boolean success;

    private String message;

    @JsonPropertyOrder("scrap_id")
    private Long scrapId;
}
