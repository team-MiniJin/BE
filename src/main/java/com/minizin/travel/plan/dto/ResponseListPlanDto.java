package com.minizin.travel.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// #29 2024.06.02 내 여행 일정 조회 START //
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListPlanDto {

    List<ListPlanDto> data;

    Long nextCursor;
}
// #29 2024.06.02 내 여행 일정 조회 END //