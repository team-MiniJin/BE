package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// #29 2024.06.02 내 여행 일정 조회 START //

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSelectListPlanDto {

    // success
    private List<SelectListPlanDto> data;

    private Long nextCursor;

    // fail
    private Boolean success;

    private String message;

    private Long userId;

}
// #29 2024.06.02 내 여행 일정 조회 END //