package com.minizin.travel.tour.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tour_api")
public class TourAPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long tourId;

    @Column(name = "content_id")
    private String contentId;

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
    private String listYn;

    @Column(name = "map_x")
    private String mapX;

    @Column(name = "map_y")
    private String mapY;

    @Column(name = "mobile_app")
    private String mobileApp;

    @Column(name = "mobile_os")
    private String mobileOs;

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

    @Column(name = "default_yn")
    private String defaultYn;

    @Column(name = "first_image_yn")
    private String firstImageYn;

    @Column(name = "area_code_yn")
    private String areaCodeYn;

    @Column(name = "cat_code_yn")
    private String catCodeYn;

    @Column(name = "addr_info_yn")
    private String addrInfoYn;

    @Column(name = "map_info_yn")
    private String mapInfoYn;

    @Column(name = "overview_yn")
    private String overviewYn;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "benikia")
    private String benikia;

    @Column(name = "booktour")
    private String booktour;

    @Column(name = "code")
    private String code;

    @Column(name = "cpyrht_div_cd")
    private String cpyrhtDivCd;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "dist")
    private double dist;

    @Column(name = "first_image")
    private String firstImage;

    @Column(name = "first_image2")
    private String firstImage2;

    @Column(name = "good_stay")
    private String goodStay;

    @Column(name = "hanok")
    private String hanok;

    @Column(name = "mlevel")
    private String mlevel;

    @Column(name = "name")
    private String name;

    @Column(name = "rnum")
    private Integer rnum;

    @Column(name = "tel")
    private String tel;

    @Column(name = "title")
    private String title;

    @Column(name = "total_cnt")
    private int totalCnt;

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "tel_name")
    private String telName;

    @Column(name = "overview")
    private String overview;
}
