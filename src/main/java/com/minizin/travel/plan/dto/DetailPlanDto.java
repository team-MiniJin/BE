package com.minizin.travel.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.plan.entity.Plan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "userId", "planName", "theme", "startDate", "endDate", "planBudget", "scope", "numberOfMembers",
        "numberOfScraps", "regionList", "detailPlanScheduleDtoList"})
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetailPlanDto {

    @JsonProperty("plan_id")
    private Long id;

    private Long userId;

    private String planName;

    @Setter
    private String userNickname;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private Integer planBudget;

    private boolean scope;

    private Integer numberOfMembers;

    private Integer numberOfScraps;

    @Setter
    private List<String> regionList;

    @Setter
    @JsonProperty("schedules")
    private List<DetailPlanScheduleDto> detailPlanScheduleDtoList;

    private Integer error;
    private String message;

    public static DetailPlanDto toDto(Plan plan) {
        return DetailPlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .planName(plan.getPlanName())
                .theme(plan.getTheme())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .scope(plan.isScope())
                .numberOfMembers(plan.getNumberOfMembers())
                .numberOfScraps(plan.getNumberOfScraps())
                .build();
    }

    public static DetailPlanDto existsNot(Long planId) {

        return DetailPlanDto.builder()
                .error(-1)
                .id(planId)
                .message("요청하신 plan 은 존재하지 않습니다.")
                .build();
    }

    public static DetailPlanDto notAuth(Long planId, String message) {

        return DetailPlanDto.builder()
                .error(-2)
                .id(planId)
                .message(message)
                .build();
    }

}
