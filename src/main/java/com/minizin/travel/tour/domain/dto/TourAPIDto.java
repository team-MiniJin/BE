package com.minizin.travel.tour.domain.dto;

import com.minizin.travel.tour.domain.entity.TourAPI;
import java.util.ArrayList;
import java.util.List;
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

    private Long tourId;
    private String _type;
    private String areaCode;
    private String arrange;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentId;
    private String contentTypeId;
    private String eventStartDate;
    private String keyword;
    private String listYN;
    private String mapX;
    private String mapY;
    private String mobileApp;
    private String mobileOS;
    private String modifiedTime;
    private Integer numOfRows;
    private Integer pageNo;
    private String radius;
    private String serviceKey;
    private String sigunguCode;
    private Response response;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String _type;
        private Long tourId;
        private String contentId;
        private String areaCode;
        private String arrange;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentTypeId;
        private String eventStartDate;
        private String keyword;
        private String listYN;
        private String mapX;
        private String mapY;
        private String mobileApp;
        private String mobileOS;
        private String modifiedTime;
        private Integer numOfRows;
        private Integer pageNo;
        private String radius;
        private String serviceKey;
        private String sigunguCode;

        public TourAPI toEntity() {
            return TourAPI.builder()
                .tourId(tourId)
                ._type(_type)
                .areaCode(areaCode)
                .arrange(arrange)
                .cat1(cat1)
                .cat2(cat2)
                .cat3(cat3)
                .contentTypeId(contentTypeId)
                .eventStartDate(eventStartDate)
                .keyword(keyword)
                .listYn(listYN)
                .mapX(mapX)
                .mapY(mapY)
                .mobileApp(mobileApp)
                .mobileOs(mobileOS)
                .modifiedTime(modifiedTime)
                .numOfRows(numOfRows != null ? numOfRows : 0) // Provide default value
                .pageNo(pageNo != null ? pageNo : 0)
                .radius(radius != null ? radius : "0")
                .serviceKey(serviceKey)
                .sigunguCode(sigunguCode)
                .build();
        }
    }

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
                private Item[] item;

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                @Builder
                public static class Item {
                    private int rnum;
                    private String code;
                    private String name;

                    public TourAPI toEntity() {
                        return TourAPI.builder()
                            .areaCode(code)
                            .name(name)
                            .rnum(rnum)
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
