package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponsePlanDto {

    private boolean success;
    private String message;

    private Long planId;

    private Integer numberOfScraps; // #87 Request 예외/에러 처리

    private String createAt;

    private String updatedAt;

    private Data data;

    @Builder
    @Getter
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Data {

        private String startDate;

        private String endDate;

        private String scheduleDate;
    }

    public static ResponsePlanDto success(Plan plan) {

        return ResponsePlanDto.builder()
                .success(true)
                .message("일정을 생성하였습니다.")
                .planId(plan.getId())
                .numberOfScraps(plan.getNumberOfScraps())
                .createAt(plan.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(plan.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static ResponsePlanDto fail(ResponsePlanDto.Data data) {

        return ResponsePlanDto.builder()
                .success(false)
                .message("날짜가 유효하지 않습니다.")
                .data(data).build();
    }

    public static ResponsePlanDto copySuccess(Plan plan) {

        return ResponsePlanDto.builder()
                .success(true)
                .message("일정을 복사하였습니다.")
                .planId(plan.getId())
                .numberOfScraps(plan.getNumberOfScraps())
                .createAt(plan.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(plan.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static ResponsePlanDto existsNot(Long planId) {

        return ResponsePlanDto.builder()
                .success(false)
                .message("요청하신 plan 은 존재하지 않습니다.")
                .planId(planId)
                .build();
    }
}
