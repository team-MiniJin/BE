package com.minizin.travel.tour.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Class: TourAPIRequest Project: travel Package: com.minizin.travel.tour.domain.entity
 * <p>
 * Description: TourAPIRequest
 *
 * @author dong-hoshin
 * @date 6/3/24 19:23 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Getter
@Entity
@Table(name = "tour_api_request")
public class TourAPIRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "type")
    private String _type;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "arrange")
    private String arrange;

    @Column(name = "cat1")
    private String cat1;

    @Column(name = "cat2")
    private String cat2;

    @Column(name = "cat3")
    private String cat3;

    @Column(name = "content_type_id")
    private String contentTypeId;

    @Column(name = "event_start_date")
    private String eventStartDate;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "list_yn")
    private String listYN;

    @Column(name = "map_x")
    private String mapX;

    @Column(name = "map_y")
    private String mapY;

    @Column(name = "mobile_app")
    private String mobileApp;

    @Column(name = "mobile_os")
    private String mobileOS;

    @Column(name = "modified_time")
    private String modifiedTime;

    @Column(name = "num_of_rows")
    private Integer numOfRows;

    @Column(name = "page_no")
    private Integer pageNo;

    @Column(name = "radius")
    private String radius;

    @Column(name = "service_key")
    private String serviceKey;

    @Column(name = "sigungu_code")
    private String sigunguCode;

}
