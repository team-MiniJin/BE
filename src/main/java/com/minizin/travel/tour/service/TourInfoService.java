package com.minizin.travel.tour.service;

import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.domain.repository.TourAPIRepository;
import java.io.IOException;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Class: TourInfoService Project: travel Package: com.minizin.travel.tour.service
 * <p>
 * Description: TourInfoService
 *
 * @author dong-hoshin
 * @date 6/16/24 13:49 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Service
@RequiredArgsConstructor
public class TourInfoService {

    private final TourAPIRepository tourAPIRepository;

    public TourAPIDto getTourDataByAreaCode(TourAPIDto.TourRequest requestUrl) throws IOException {
        int pageNo = 0;
        String areaCode = Optional.ofNullable(requestUrl.getAreaCode()).orElse("");

        // 데이터베이스에서 중복 제거된 데이터 가져오기
        List<TourAPI> rawEntities = tourAPIRepository.findDistinctAreaCode();
        int numOfRows = 100;

        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .filter(tourAPI -> {
                // areaCode 있을 때 (sigunguCode가 존재 && areaCode와 동일한 Code) Data만 가져오기.
                if (!areaCode.isEmpty()) {
                    return !tourAPI.getSigunguCode().isEmpty() && tourAPI.getCode().equals(areaCode);
                    //areaCode가 없을 때 (sigunguCode가 없는) data가져오기.
                } else {
                    return tourAPI.getSigunguCode().isEmpty();
                }
            })
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        // 응답 객체 생성
        TourAPIDto responseDto = buildResponseWithTourAPIDto(rawItems, pageNo,numOfRows);

        return responseDto;
    }

    public TourAPIDto getTourDataByDetailCommon(TourAPIDto.TourRequest requestUrl) throws IOException {
        String pageNo = Optional.ofNullable(requestUrl.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestUrl.getNumOfRows()).orElse("10");
        String contentId = Optional.ofNullable(requestUrl.getContentId()).orElse("");

        int page = Integer.parseInt(pageNo);
        int size = Integer.parseInt(numOfRows);

        List<TourAPI> rawEntities;
        // 데이터베이스에서 중복 제거된 데이터 가져오기
        // contentId가 있을 때는 contentId 기반으로 가져오고
        // 없을 때는 모든 자료를 가져오기.
        if (contentId != "") {
            rawEntities = tourAPIRepository.findByContentId(contentId);
        } else {
            rawEntities = tourAPIRepository.findAllList();
        }

        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = buildResponseWithTourAPIDto(sortedItems, page, size);

        return responseDto;
    }

    public TourAPIDto getTourDataByAreaBasedList(TourAPIDto.TourRequest requestUrl) throws IOException {
        String pageNo = Optional.ofNullable(requestUrl.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestUrl.getNumOfRows()).orElse("10");
        String areaCode = Optional.ofNullable(requestUrl.getAreaCode()).orElse("");
        String contentTypeId = Optional.ofNullable(requestUrl.getContentTypeId()).orElse("");
        String sigunguCode = Optional.ofNullable(requestUrl.getSigunguCode()).orElse("");

        int page = Integer.parseInt(pageNo);
        int size = Integer.parseInt(numOfRows);
        List<TourAPI> rawEntities;

        // 데이터베이스에서 중복 제거된 데이터 가져오기
        // areaCode가 있을 때는 areaCode기반으로 가져오고
        // 없을 때는 모든 자료를 가져오기.
        if (areaCode != "") {
            rawEntities = tourAPIRepository.findDistinctAreaBasedList(areaCode);
        } else {
            rawEntities = tourAPIRepository.findAllList();
        }
        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .filter(tourAPI ->
                // 변수값이 비어있을 때는 오른쪽 filter조건을 건너뜀
                // 변수값이 비어있지 않은 조건들을 모두 만족 시킨 데이터만 필터링함.
                (areaCode.isEmpty() || tourAPI.getAreaCode().equals(areaCode)) &&
                    (contentTypeId.isEmpty() || tourAPI.getContentTypeId().equals(contentTypeId)) &&
                    (sigunguCode.isEmpty() || tourAPI.getSigunguCode().equals(sigunguCode))
            )
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = buildResponseWithTourAPIDto(sortedItems, page, size);

        return responseDto;
    }

    public TourAPIDto getTourDataSearchKeyword(TourAPIDto.TourRequest requestUrl) throws IOException {
        String pageNo = Optional.ofNullable(requestUrl.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestUrl.getNumOfRows()).orElse("10");
        String keyword = Optional.ofNullable(requestUrl.getKeyword()).orElse("");
        String areaCode = Optional.ofNullable(requestUrl.getAreaCode()).orElse("");
        String contentTypeId = Optional.ofNullable(requestUrl.getContentTypeId()).orElse("");
        String sigunguCode = Optional.ofNullable(requestUrl.getSigunguCode()).orElse("");

        int page = Integer.parseInt(pageNo);
        int size = Integer.parseInt(numOfRows);

        // 데이터베이스에서 중복 제거된 데이터 가져오기
        List<TourAPI> rawEntities = tourAPIRepository.findAllList();

        // 변수값이 비어있을 때는 오른쪽 filter조건을 건너뜀
        // 변수값이 비어있지 않은 조건들을 모두 만족 시킨 데이터만 필터링함.
        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .filter(tourAPI ->
                (areaCode.isEmpty() || tourAPI.getAreaCode().equals(areaCode)) &&
                    (contentTypeId.isEmpty() || tourAPI.getContentTypeId().equals(contentTypeId)) &&
                    (sigunguCode.isEmpty() || tourAPI.getSigunguCode().equals(sigunguCode)) &&
                    (keyword.isEmpty() || (tourAPI.getAddr1().contains(keyword) || tourAPI.getTitle().contains(keyword)))
            )
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = buildResponseWithTourAPIDto(sortedItems, page, size);

        return responseDto;
    }

    private List<TourAPIDto.TourResponse.Body.Items.Item> sortItemsByTitle(List<TourAPIDto.TourResponse.Body.Items.Item> items) {
        Collator koreanCollator = Collator.getInstance(Locale.KOREAN);

        Comparator<TourAPIDto.TourResponse.Body.Items.Item> customComparator = Comparator
            //한글, 영문자, 숫자, 기타 순으로 정렬.
            .comparing((TourAPIDto.TourResponse.Body.Items.Item item) -> {
                String title = item.getTitle();
                if (title.matches("^[가-힣ㄱ-ㅎ].*")) return 0;
                if (title.matches("^[a-zA-Z].*")) return 1;
                if (title.matches("^[0-9].*")) return 2;
                return 3;
            })
            // 각 그룹 내 정렬 기준에 맞춰 정렬
            .thenComparing((item1, item2) -> {
                String title1 = item1.getTitle();
                String title2 = item2.getTitle();
                if (title1.matches("^[가-힣ㄱ-ㅎ].*") && title2.matches("^[가-힣ㄱ-ㅎ].*")) {
                    return koreanCollator.compare(title1, title2);
                }
                if (title1.matches("^[a-zA-Z].*") && title2.matches("^[a-zA-Z].*")) {
                    return title1.compareToIgnoreCase(title2);
                }
                return title1.compareTo(title2);
            });

        return items.stream()
            .sorted(customComparator)
            .collect(Collectors.toList());
    }


        private TourAPIDto buildResponseWithTourAPIDto(List<TourAPIDto.TourResponse.Body.Items.Item> itemList, int pageNo, int numOfRows) {
        // 객체 page별로 반환
        int start = Math.min(pageNo * numOfRows, itemList.size());
        int end = Math.min((pageNo + 1) * numOfRows, itemList.size());
        List<TourAPIDto.TourResponse.Body.Items.Item> pagedItems = itemList.subList(start, end);

        // Items 객체로 변환
        TourAPIDto.TourResponse.Body.Items items = TourAPIDto.TourResponse.Body.Items.builder()
            .item(pagedItems)
            .build();

        // Body 객체 생성
        TourAPIDto.TourResponse.Body body = TourAPIDto.TourResponse.Body.builder()
            .items(items)
            .numOfRows(numOfRows)
            .pageNo(pageNo)
            .totalCount(itemList.size())
            .build();

        // Header 객체 생성
        TourAPIDto.TourResponse.Header header = TourAPIDto.TourResponse.Header.builder()
            .resultCode("0000")
            .resultMsg("OK")
            .build();

        // 최종 응답 객체 생성
        return TourAPIDto.builder()
            .response(TourAPIDto.TourResponse.builder()
                .header(header)
                .body(body)
                .build())
            .build();
    }

}
