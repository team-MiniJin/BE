package com.minizin.travel.tour.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.dto.TourAPIDto.TourRequest;
import com.minizin.travel.tour.domain.dto.TourAPIDto.TourResponse;
import com.minizin.travel.tour.domain.repository.TourAPIRepository;
import java.io.IOException;
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
        List<Object[]> rawItems = tourAPIRepository.findDistinctAreaCode();
        List<TourAPIDto.TourResponse.Body.Items.Item> itemList = rawItems.stream()
            .map(result -> TourAPIDto.TourResponse.Body.Items.Item.builder()
                .code((String) result[0])
                .name((String) result[1])
                .rnum((Integer) result[2])
                .build())
            .collect(Collectors.toList());

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
        TourAPIDto responseDto = TourAPIDto.builder()
            .response(TourAPIDto.TourResponse.builder()
                .header(header)
                .body(body)
                .build())
            .build();

        System.out.println(responseDto.toString());
        return responseDto;
    }

    private String requestUrlCombine(String getCategoryUrl, TourRequest requestUrl) {
        // requestUrl을 Map으로 변환
        Map<String, String> paramMap = objectMapper.convertValue(requestUrl, Map.class);

        // 빈값이나 null값은 제외
        Map<String, String> filteredMap = paramMap.entrySet().stream()
            .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        HttpUrl.Builder urlBuilder = HttpUrl.parse(getCategoryUrl).newBuilder();
        // Map의 각 엔트리를 URL 빌더에 추가
        filteredMap.forEach(urlBuilder::addQueryParameter);

        return urlBuilder.build().toString();
    }
}
