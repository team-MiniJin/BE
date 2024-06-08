
package com.minizin.travel.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "planSchedule")
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PlanSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "place_category")
    private String placeCategory;

    @Column(name = "place_name")
    private String placeName;

    private String region;

    @Column(name = "place_memo")
    private String placeMemo;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    private Double x;

    private Double y;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDate modifiedAt;
}