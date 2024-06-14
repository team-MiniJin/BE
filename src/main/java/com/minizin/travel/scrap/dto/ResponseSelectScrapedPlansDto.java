package com.minizin.travel.scrap.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseSelectScrapedPlansDto {

    private List<SelectScrapedPlansDto> data;

    private Long cursorId;
}
