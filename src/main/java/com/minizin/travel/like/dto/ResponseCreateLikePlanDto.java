package com.minizin.travel.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.like.entity.Likes;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseCreateLikePlanDto {

    @JsonProperty("like_id")
    private Long id;

    private Long userId;

    private Long planId;

    private String createdAt;

    public static ResponseCreateLikePlanDto toDto(Likes like) {
        return ResponseCreateLikePlanDto.builder()
                .id(like.getId())
                .userId(like.getUserId())
                .planId(like.getPlanId())
                .createdAt(like.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
