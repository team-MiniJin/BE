package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// #48 2024.06.10 다른 사람 여행 일정 조회 START //
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseOthersPlanDto {

    List<OthersListPlanDto> data;

    Long nextCursor;
}
// #48 2024.06.10 다른 사람 여행 일정 조회 END //