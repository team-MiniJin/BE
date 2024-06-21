package com.minizin.travel.tour.domain.entity;

import com.minizin.travel.tour.domain.dto.TourAPIDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private String dist;

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

    @Column(name = "homepage", columnDefinition = "TEXT")
    private String homepage;

    @Column(name = "tel_name")
    private String telName;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    public TourAPIDto.TourResponse.Body.Items.Item toDto() {
        return TourAPIDto.TourResponse.Body.Items.Item.builder()
            .addr1(Optional.ofNullable(this.getAddr1()).orElse(""))
            .addr2(Optional.ofNullable(this.getAddr2()).orElse(""))
            .areacode(Optional.ofNullable(this.getAreaCode()).orElse(""))
            .benikia(Optional.ofNullable(this.getBenikia()).orElse(""))
            .booktour(Optional.ofNullable(this.getBooktour()).orElse(""))
            .cat1(Optional.ofNullable(this.getCat1()).orElse(""))
            .cat2(Optional.ofNullable(this.getCat2()).orElse(""))
            .cat3(Optional.ofNullable(this.getCat3()).orElse(""))
            .code(Optional.ofNullable(this.getCode()).orElse(""))
            .contentid(Optional.ofNullable(this.getContentId()).orElse(""))
            .contenttypeid(Optional.ofNullable(this.getContentTypeId()).orElse(""))
            .cpyrhtDivCd(Optional.ofNullable(this.getCpyrhtDivCd()).orElse(""))
            .createdtime(Optional.ofNullable(this.getCreatedTime()).orElse(""))
            .dist(Optional.ofNullable(this.getDist()).orElse(""))
            .eventstartdate(Optional.ofNullable(this.getEventStartDate()).orElse(""))
            .firstimage(Optional.ofNullable(this.getFirstImage()).orElse(""))
            .firstimage2(Optional.ofNullable(this.getFirstImage2()).orElse(""))
            .goodstay(Optional.ofNullable(this.getGoodStay()).orElse(""))
            .hanok(Optional.ofNullable(this.getHanok()).orElse(""))
            .mapx(Optional.ofNullable(this.getMapX()).orElse(""))
            .mapy(Optional.ofNullable(this.getMapY()).orElse(""))
            .mlevel(Optional.ofNullable(this.getMlevel()).orElse(""))
            .modifiedtime(Optional.ofNullable(this.getModifiedTime()).orElse(""))
            .name(Optional.ofNullable(this.getName()).orElse(""))
            .numOfRows(Optional.ofNullable(this.getNumOfRows()).orElse(0))
            .pageNo(Optional.ofNullable(this.getPageNo()).orElse(0))
            .rnum(Optional.ofNullable(this.getRnum()).orElse(0))
            .sigungucode(Optional.ofNullable(this.getSigunguCode()).orElse(""))
            .tel(Optional.ofNullable(this.getTel()).orElse(""))
            .title(Optional.ofNullable(this.getTitle()).orElse(""))
            .totalCnt(Optional.ofNullable(this.getTotalCnt()).orElse(0))
            .totalCount(Optional.ofNullable(this.getTotalCount()).orElse(0))
            .zipcode(Optional.ofNullable(this.getZipcode()).orElse(""))
            .homepage(Optional.ofNullable(this.getHomepage()).orElse(""))
            .telname(Optional.ofNullable(this.getTelName()).orElse(""))
            .overview(Optional.ofNullable(this.getOverview()).orElse(""))
            .build();
    }

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    public CompletableFuture<Void> updateFromDtoAsync(TourAPIDto.TourResponse.Body.Items.Item dto) {
        return CompletableFuture.runAsync(() -> {
            if (dto.getAddr1() != null) this.addr1 = dto.getAddr1();
            if (dto.getAddr2() != null) this.addr2 = dto.getAddr2();
            if (dto.getAreacode() != null) this.areaCode = dto.getAreacode();
            if (dto.getBenikia() != null) this.benikia = dto.getBenikia();
            if (dto.getBooktour() != null) this.booktour = dto.getBooktour();
            if (dto.getCat1() != null) this.cat1 = dto.getCat1();
            if (dto.getCat2() != null) this.cat2 = dto.getCat2();
            if (dto.getCat3() != null) this.cat3 = dto.getCat3();
            if (dto.getCode() != null) this.code = dto.getCode();
            if (dto.getContentid() != null) this.contentId = dto.getContentid();
            if (dto.getContenttypeid() != null) this.contentTypeId = dto.getContenttypeid();
            if (dto.getCpyrhtDivCd() != null) this.cpyrhtDivCd = dto.getCpyrhtDivCd();
            if (dto.getCreatedtime() != null) this.createdTime = dto.getCreatedtime();
            if (dto.getDist() != null) this.dist = dto.getDist();
            if (dto.getEventstartdate() != null) this.eventStartDate = dto.getEventstartdate();
            if (dto.getFirstimage() != null) this.firstImage = dto.getFirstimage();
            if (dto.getFirstimage2() != null) this.firstImage2 = dto.getFirstimage2();
            if (dto.getGoodstay() != null) this.goodStay = dto.getGoodstay();
            if (dto.getHanok() != null) this.hanok = dto.getHanok();
            if (dto.getMapx() != null) this.mapX = dto.getMapx();
            if (dto.getMapy() != null) this.mapY = dto.getMapy();
            if (dto.getMlevel() != null) this.mlevel = dto.getMlevel();
            if (dto.getModifiedtime() != null) this.modifiedTime = dto.getModifiedtime();
            if (dto.getName() != null) this.name = dto.getName();
            if (dto.getNumOfRows() != null) this.numOfRows = dto.getNumOfRows();
            if (dto.getPageNo() != null) this.pageNo = dto.getPageNo();
            if (dto.getRnum() != null) this.rnum = dto.getRnum();
            if (dto.getSigungucode() != null) this.sigunguCode = dto.getSigungucode();
            if (dto.getTel() != null) this.tel = dto.getTel();
            if (dto.getTitle() != null) this.title = dto.getTitle();
            if (dto.getTotalCnt() != null) this.totalCnt = dto.getTotalCnt();
            if (dto.getTotalCount() != null) this.totalCount = dto.getTotalCount();
            if (dto.getZipcode() != null) this.zipcode = dto.getZipcode();
            if (dto.getHomepage() != null) this.homepage = dto.getHomepage();
            if (dto.getTelname() != null) this.telName = dto.getTelname();
            if (dto.getOverview() != null) this.overview = dto.getOverview();
        }, threadPool);
    }
}