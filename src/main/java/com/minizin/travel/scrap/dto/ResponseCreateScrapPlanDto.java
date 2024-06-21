package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minizin.travel.scrap.entity.Scrap;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCreateScrapPlanDto {

    @JsonProperty("scrap_id")
    private Long id;

    private Long userId;

    private Long planId;

    private String createdAt;

    private boolean success;

    private String message;

    public static ResponseCreateScrapPlanDto toDto(Scrap scrap) {
        return ResponseCreateScrapPlanDto.builder()
                .success(true)
                .id(scrap.getId())
                .userId(scrap.getUserId())
                .planId(scrap.getPlanId())
                .createdAt(scrap.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static ResponseCreateScrapPlanDto fail(Long planId, String message) {

        return ResponseCreateScrapPlanDto.builder()
                .success(false)
                .message(message)
                .id(planId)
                .build();
    }
}
