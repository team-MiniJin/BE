package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseSelectScrapedPlansDto {

    private List<SelectScrapedPlansDto> data;

    private Long cursorId;

    private String message;

    @JsonProperty("scrap_id")
    private Long id;

    public static ResponseSelectScrapedPlansDto fail(String message) {

        return ResponseSelectScrapedPlansDto.builder()
                .message(message)
                .build();
    }
}
