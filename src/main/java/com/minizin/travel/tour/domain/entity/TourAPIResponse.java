package com.minizin.travel.tour.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Class: TourAPIResponse Project: travel Package: com.minizin.travel.tour.domain.entity
 * <p>
 * Description: TourAPIResponse
 *
 * @author dong-hoshin
 * @date 6/3/24 19:46 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Entity
@Table(name = "tour_api_response")
public class TourAPIResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id", nullable = false)
    private Long responseId;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "benikia")
    private String benikia;

    @Column(name = "booktour")
    private String booktour;

    @Column(name = "cat1")
    private String cat1;

    @Column(name = "cat2")
    private String cat2;

    @Column(name = "cat3")
    private String cat3;

    @Column(name = "code")
    private String code;

    @Column(name = "content_id")
    private String contentId;

    @Column(name = "content_type_id")
    private String contentTypeId;

    @Column(name = "cpyrht_div_cd")
    private String cpyrhtDivCd;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "dist")
    private String dist;

    @Column(name = "event_start_date")
    private String eventStartDate;

    @Column(name = "first_image")
    private String firstImage;

    @Column(name = "first_image2")
    private String firstImage2;

    @Column(name = "good_stay")
    private String goodStay;

    @Column(name = "hanok")
    private String hanok;

    @Column(name = "mapx")
    private String mapx;

    @Column(name = "mapy")
    private String mapy;

    @Column(name = "mlevel")
    private String mlevel;


    @Column(name = "modified_time")
    private String modifiedTime;


    @Column(name = "name")
    private String name;

    @Column(name = "num_of_rows")
    private Integer numOfRows;

    @Column(name = "page_no")
    private Integer pageNo;

    @Column(name = "result_code")
    private String resultCode;

    @Column(name = "result_msg")
    private String resultMsg;

    @Column(name = "rnum")
    private String rnum;

    @Column(name = "sigungu_code")
    private String sigunguCode;

    @Column(name = "tel")
    private String tel;

    @Column(name = "title")
    private String title;

    @Column(name = "total_cnt")
    private Integer totalCnt;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "zipcode")
    private String zipcode;

}
