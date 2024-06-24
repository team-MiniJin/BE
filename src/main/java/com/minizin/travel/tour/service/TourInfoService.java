package com.minizin.travel.tour.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.dto.TourAPIDto.TourRequest;
import com.minizin.travel.tour.domain.dto.TourAPIDto.TourResponse;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.domain.repository.TourAPIRepository;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
                if (!areaCode.isEmpty()) {
                    return !tourAPI.getSigunguCode().isEmpty() && tourAPI.getCode().equals(areaCode);
                } else {
                    return tourAPI.getSigunguCode().isEmpty();
                }
            })
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        // 응답 객체 생성
        TourAPIDto responseDto = createTourAPIDto(rawItems, pageNo,numOfRows);

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
        if (contentId != "") {
            rawEntities = tourAPIRepository.findByContentId(contentId);
        } else {
            rawEntities = tourAPIRepository.findAllList();
        }

        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = createTourAPIDto(sortedItems, page, size);

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
        if (areaCode != "") {
            rawEntities = tourAPIRepository.findDistinctAreaBasedList(areaCode);
        } else {
            rawEntities = tourAPIRepository.findAllList();
        }
        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .filter(tourAPI ->
                (areaCode.isEmpty() || tourAPI.getAreaCode().equals(areaCode)) &&
                    (contentTypeId.isEmpty() || tourAPI.getContentTypeId().equals(contentTypeId)) &&
                    (sigunguCode.isEmpty() || tourAPI.getSigunguCode().equals(sigunguCode))
            )
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = createTourAPIDto(sortedItems, page, size);

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

        List<TourAPI> rawEntities;
        // 데이터베이스에서 중복 제거된 데이터 가져오기
        if (keyword != "") {
            rawEntities = tourAPIRepository.findDistinctSearchKeyword(keyword);
        } else {
            rawEntities = tourAPIRepository.findAllList();
        }

        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .filter(tourAPI ->
                (areaCode.isEmpty() || tourAPI.getAreaCode().equals(areaCode)) &&
                    (contentTypeId.isEmpty() || tourAPI.getContentTypeId().equals(contentTypeId)) &&
                    (sigunguCode.isEmpty() || tourAPI.getSigunguCode().equals(sigunguCode))
            )
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> sortedItems = sortItemsByTitle(rawItems);

        TourAPIDto responseDto = createTourAPIDto(sortedItems, page, size);

        return responseDto;
    }

    private List<TourAPIDto.TourResponse.Body.Items.Item> sortItemsByTitle(List<TourAPIDto.TourResponse.Body.Items.Item> items) {
        Collator collator = Collator.getInstance(Locale.KOREAN);

        List<TourAPIDto.TourResponse.Body.Items.Item> koreanItems = items.stream()
            .filter(item -> item.getTitle().matches("^[ㄱ-ㅎ가-힣].*"))
            .sorted(Comparator.comparing(TourAPIDto.TourResponse.Body.Items.Item::getTitle, collator))
            .collect(Collectors.toList());

        List<TourAPIDto.TourResponse.Body.Items.Item> nonKoreanItems = items.stream()
            .filter(item -> !item.getTitle().matches("^[ㄱ-ㅎ가-힣].*"))
            .sorted(Comparator.comparing(TourAPIDto.TourResponse.Body.Items.Item::getTitle, collator))
            .collect(Collectors.toList());

        koreanItems.addAll(nonKoreanItems);
        return koreanItems;
    }

        private TourAPIDto createTourAPIDto(List<TourAPIDto.TourResponse.Body.Items.Item> itemList, int pageNo, int numOfRows) {
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
