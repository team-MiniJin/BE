package com.minizin.travel.tour.domain.dto;

import com.minizin.travel.tour.domain.entity.TourAPI;
import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class: TourAPIRequestDTO Project: travel Package: com.minizin.travel.tour.domain.dto
 * <p>
 * Description: TourAPIRequestDTO
 *
 * @author dong-hoshin
 * @date 6/3/24 19:56 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourAPIDto {
    private TourRequest request;
    @Data
    @Getter
    @Setter
    public static class TourRequest {
        private String MobileOS;
        private String MobileApp;
        private String _type;
        private String ServiceKey;

        private String areaCode;
        private String arrange;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentTypeId;
        private String contentId;
        private String eventStartDate;
        private String keyword;
        private String listYN;
        private String mapX;
        private String mapY;
        private String modifiedtime;
        private String numOfRows;
        private String pageNo;
        private String radius;
        private String sigunguCode;
        private String defaultYN;
        private String firstImageYN;
        private String areacodeYN;
        private String catcodeYN;
        private String addrinfoYN;
        private String mapinfoYN;
        private String overviewYN;
    }

    private TourResponse response;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TourResponse {
        private Header header;
        private Body body;


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Body {
            private Items items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Items {
                private List<Item> item;

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                @Builder
                public static class Item {
                    private String addr1;
                    private String addr2;
                    private String areacode;
                    private String benikia;
                    private String booktour;
                    private String cat1;
                    private String cat2;
                    private String cat3;
                    private String code;
                    private String contentid;
                    private String contenttypeid;
                    private String cpyrhtDivCd;
                    private String createdtime;
                    private String dist;
                    private String eventstartdate;
                    private String firstimage;
                    private String firstimage2;
                    private String goodstay;
                    private String hanok;
                    private String mapx;
                    private String mapy;
                    private String mlevel;
                    private String modifiedtime;
                    private String name;
                    private Integer numOfRows;
                    private Integer pageNo;
                    private Integer rnum;
                    private String sigungucode;
                    private String tel;
                    private String title;
                    private Integer totalCnt;
                    private Integer totalCount;
                    private String zipcode;
                    @Column(columnDefinition = "TEXT")
                    private String homepage;
                    private String telname;
                    @Column(columnDefinition = "TEXT")
                    private String overview;
                    private String readcount;

                    public TourAPI toEntity() {
                        return TourAPI.builder()
                            .addr1(Optional.ofNullable(addr1).orElse(""))
                            .addr2(Optional.ofNullable(addr2).orElse(""))
                            .areaCode(Optional.ofNullable(areacode).orElse(""))
                            .benikia(Optional.ofNullable(benikia).orElse(""))
                            .booktour(Optional.ofNullable(booktour).orElse(""))
                            .cat1(Optional.ofNullable(cat1).orElse(""))
                            .cat2(Optional.ofNullable(cat2).orElse(""))
                            .cat3(Optional.ofNullable(cat3).orElse(""))
                            .code(Optional.ofNullable(code).orElse(""))
                            .contentId(Optional.ofNullable(contentid).orElse(""))
                            .contentTypeId(Optional.ofNullable(contenttypeid).orElse(""))
                            .cpyrhtDivCd(Optional.ofNullable(cpyrhtDivCd).orElse(""))
                            .createdTime(Optional.ofNullable(createdtime).orElse(""))
                            .dist(Optional.ofNullable(dist).orElse(""))
                            .eventStartDate(Optional.ofNullable(eventstartdate).orElse(""))
                            .firstImage(Optional.ofNullable(firstimage).orElse(""))
                            .firstImage2(Optional.ofNullable(firstimage2).orElse(""))
                            .goodStay(Optional.ofNullable(goodstay).orElse(""))
                            .hanok(Optional.ofNullable(hanok).orElse(""))
                            .mapX(Optional.ofNullable(mapx).orElse(""))
                            .mapY(Optional.ofNullable(mapy).orElse(""))
                            .mlevel(Optional.ofNullable(mlevel).orElse(""))
                            .modifiedTime(Optional.ofNullable(modifiedtime).orElse(""))
                            .name(Optional.ofNullable(name).orElse(""))
                            .numOfRows(Optional.ofNullable(numOfRows).orElse(0))
                            .pageNo(Optional.ofNullable(pageNo).orElse(0))
                            .rnum(Optional.ofNullable(rnum).orElse(0))
                            .sigunguCode(Optional.ofNullable(sigungucode).orElse(""))
                            .tel(Optional.ofNullable(tel).orElse(""))
                            .title(Optional.ofNullable(title).orElse(""))
                            .totalCnt(Optional.ofNullable(totalCnt).orElse(0))
                            .totalCount(Optional.ofNullable(totalCount).orElse(0))
                            .zipcode(Optional.ofNullable(zipcode).orElse(""))
                            .homepage(Optional.ofNullable(homepage).orElse(""))
                            .telName(Optional.ofNullable(telname).orElse(""))
                            .overview(Optional.ofNullable(overview).orElse(""))
                            .build();
                    }
                    public TourAPI toEntitySigungu(String codeArea,boolean codeOrSigungu) {
                        // areaCode 가 비어있을 때 모든 특별시, 도를 검색 (true)
                        // areaCode가 있을 때는 해당 areaCode의 sigungu를 검색 (false)
                        // !codeOrSigungu -> areaCode가 있을 때 sigungucode에 code를 넣고 code에 areaCode를 넣는다.
                        if (!codeOrSigungu) {
                            sigungucode = code;
                            code = codeArea;
                        }
                        return TourAPI.builder()
                            .code(Optional.ofNullable(code).orElse(""))
                            .sigunguCode(Optional.ofNullable(sigungucode).orElse(""))
                            .rnum(Optional.ofNullable(rnum).orElse(0))
                            .totalCnt(Optional.ofNullable(totalCnt).orElse(0))
                            .totalCount(Optional.ofNullable(totalCount).orElse(0))
                            .name(Optional.ofNullable(name).orElse(""))
                            .build();
                    }
                }
            }
        }
    }

    public List<TourAPI> toEntityList() {
        List<TourAPI> tourAPIList = new ArrayList<>();
        if (response != null && response.getBody() != null && response.getBody().getItems() != null) {
            for (TourResponse.Body.Items.Item item : response.getBody().getItems().getItem()) {
                TourAPI tourAPI = item.toEntity();
                tourAPIList.add(tourAPI);
            }
        }
        return tourAPIList;
    }

    public List<TourAPI> toEntityListArea(String codeArea,boolean codeOrSigungu) {
        List<TourAPI> tourAPIList = new ArrayList<>();
        if (response != null && response.getBody() != null && response.getBody().getItems() != null) {
            for (TourResponse.Body.Items.Item item : response.getBody().getItems().getItem()) {
                TourAPI tourAPI = item.toEntitySigungu(codeArea, codeOrSigungu);

                tourAPIList.add(tourAPI);
            }
        }
        return tourAPIList;
    }
}
