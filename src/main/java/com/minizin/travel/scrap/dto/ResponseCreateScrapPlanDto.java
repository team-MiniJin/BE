package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minizin.travel.scrap.entity.Scrap;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class ResponseCreateScrapPlanDto {

    @JsonProperty("scrap_id")
    private Long id;

    private Long userId;

    private Long planId;

    private String createdAt;

    public static ResponseCreateScrapPlanDto toDto(Scrap scrap) {
        return ResponseCreateScrapPlanDto.builder()
                .id(scrap.getId())
                .userId(scrap.getUserId())
                .planId(scrap.getPlanId())
                .createdAt(scrap.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
