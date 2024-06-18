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
import java.util.HashMap;
import java.util.List;
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
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final TourAPIRepository tourAPIRepository;
    private final String myAPIUrl = "http://lyckabc.synology.me:20280/";
    private final ObjectMapper objectMapper;

    public TourAPIDto getTourDataByAreaCode() throws IOException {
        int pageNo = 1;

        // 데이터베이스에서 중복 제거된 데이터 가져오기
        List<TourAPI> rawEntities = tourAPIRepository.findDistinctAreaCode();

        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .map(TourAPI::toDto)
            .collect(Collectors.toList());

        // 응답 객체 생성
        TourAPIDto responseDto = createTourAPIDto(rawItems, pageNo);

        return responseDto;
    }

    public TourAPIDto getTourDataByAreaBasedList(TourAPIDto.TourRequest requestUrl) throws IOException {
        String pageNo = Optional.ofNullable(requestUrl.getPageNo()).orElse("1");
        String numOfRows = Optional.ofNullable(requestUrl.getNumOfRows()).orElse("100");
        int totalCount = Integer.parseInt(pageNo) * Integer.parseInt(numOfRows);
        String areaCode = Optional.ofNullable(requestUrl.getAreaCode()).orElse("1");

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNo), totalCount);
        // 데이터베이스에서 중복 제거된 데이터 가져오기
        List<TourAPI> rawEntities = tourAPIRepository.findDistinctAreaBasedList(areaCode, pageable);
        List<TourAPIDto.TourResponse.Body.Items.Item> rawItems = rawEntities.stream()
            .map(TourAPI::toDto)
            .collect(Collectors.toList());


        TourAPIDto responseDto = createTourAPIDto(rawItems, Integer.parseInt(pageNo));

        return responseDto;
    }

        private TourAPIDto createTourAPIDto(List<TourAPIDto.TourResponse.Body.Items.Item> itemList, int pageNo) {
        // Items 객체로 변환
        TourAPIDto.TourResponse.Body.Items items = TourAPIDto.TourResponse.Body.Items.builder()
            .item(itemList)
            .build();

        // Body 객체 생성
        TourAPIDto.TourResponse.Body body = TourAPIDto.TourResponse.Body.builder()
            .items(items)
            .numOfRows(items.getItem().size())
            .pageNo(Optional.ofNullable(pageNo).orElse(1))
            .totalCount(items.getItem().size())
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
