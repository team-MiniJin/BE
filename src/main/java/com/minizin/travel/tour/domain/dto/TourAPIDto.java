package com.minizin.travel.tour.domain.dto;

import com.minizin.travel.tour.domain.entity.TourAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourAPIDto {

    private Response response;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
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
                    private double dist;
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
                    private String homepage;
                    private String telname;
                    private String overview;
                    private int readcount;

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
                            .dist(Optional.ofNullable(dist).orElse((double)0))
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
                }
            }
        }
    }

    public List<TourAPI> toEntityList() {
        List<TourAPI> tourAPIList = new ArrayList<>();
        if (response != null && response.getBody() != null && response.getBody().getItems() != null) {
            for (Response.Body.Items.Item item : response.getBody().getItems().getItem()) {
                TourAPI tourAPI = item.toEntity();
                tourAPIList.add(tourAPI);
            }
        }
        return tourAPIList;
    }
}