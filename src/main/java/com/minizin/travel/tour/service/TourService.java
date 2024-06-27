package com.minizin.travel.tour.service;


import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.domain.repository.TourAPIRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class: TourService Project: travel Package: com.minizin.travel.tour.service
 * <p>
 * Description: TourService
 *
 * @author dong-hoshin
 * @date 6/5/24 01:04 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Service
@RequiredArgsConstructor
public class TourService {

    @Value("${api-tour.serviceKey_De}")
    public String serviceKey;
    private final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(6000, TimeUnit.SECONDS)
        .readTimeout(6000, TimeUnit.SECONDS)
        .writeTimeout(6000, TimeUnit.SECONDS)
        .build();
    private final Gson gson = new Gson();
    private final String baseUrl = "https://apis.data.go.kr/B551011/KorService1/";
    private final TourAPIRepository tourAPIRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    Logger logger = LoggerFactory.getLogger(getClass());
    private static final int MAX_RETRIES = 3;

    public CompletableFuture<String> getTourAPIFromSiteDetailCommon(TourAPIDto.TourRequest requestParam) {
        String getCategoryUrl = baseUrl + "detailCommon1";
        // 기존 TourAPI entity(DB)에 detail 정보 추가해서 DB업데이트 하기.
        long startTime = System.currentTimeMillis();
        int pageNo = Integer.parseInt(Optional.ofNullable(requestParam.getPageNo()).orElse("0"));
        int pageSize = Integer.parseInt(Optional.ofNullable(requestParam.getNumOfRows()).orElse("1000"));

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        // overview 정보가 추가 안된 데이터만 중복값 없이 가져오기
        // (pageable 한 이유는 간혹 DB 업데이트가 안되는 data들을 page를 통해 건너뛰기 위함)
        Page<TourAPI> tourAPIPage = tourAPIRepository.findAll(pageRequest);
        List<TourAPI> tourAPIs = tourAPIPage.getContent();

        //가져온 TourAPI 목록의 모든 데이터들을 비동기 방식으로 공공데이터 api에 정보 요청
        List<CompletableFuture<Void>> futures = tourAPIs.stream()
            .map(tourAPI -> {
                String contentId = tourAPI.getContentId();
                String contentTypeId = tourAPI.getContentTypeId();

                Map<String, String> params = Map.ofEntries(
                    Map.entry("ServiceKey", serviceKey),
                    Map.entry("MobileOS", "ETC"),
                    Map.entry("MobileApp", "AppTest"),
                    Map.entry("_type", "json"),
                    Map.entry("contentId", contentId),
                    Map.entry("contentTypeId", contentTypeId),
                    Map.entry("defaultYN", "Y"),
                    Map.entry("firstImageYN", "Y"),
                    Map.entry("areacodeYN", "Y"),
                    Map.entry("catcodeYN", "Y"),
                    Map.entry("addrinfoYN", "Y"),
                    Map.entry("mapinfoYN", "Y"),
                    Map.entry("overviewYN", "Y"),
                    Map.entry("pageNo", "0")
                );
                // 각 Entity Data별로 공공데이터 Request url에 맞춰 request param을 build 하기.
                String url = buildUrlWithParams(getCategoryUrl, params);
                // 각 Entity Data별로 공공데이터에 요청한 detail정보 update하기.
                return fetchAndUpdateTourAPIData(url, tourAPI);
            })
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.info("getTourAPIFromSiteDetailCommon executed in " + duration + " ms");
                if (!tourAPIs.isEmpty()) {
                    // 비동기 방식으로 가져온 모든 업데이트된 정보들을 한번에 저장.
                    tourAPIRepository.saveAll(tourAPIs);
                }
                return "getTourAPIFromSiteDetailCommon executed successfully";
            });
    }
    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteSearchKeyword(TourAPIDto.TourRequest requestParam) {
        //baseUrl에 공공데이터의 request url에 맞춰 카테고리url 넣기
        // + requestParam에 들어간 keyword, pageNo,numOfRows에 맞춰 공공데이터에서 값 가져오기
        String getCategoryUrl = baseUrl + "searchKeyword1";
        String keyword = Optional.ofNullable(requestParam.getKeyword()).orElse("강원");
        String pageNo = Optional.ofNullable(requestParam.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestParam.getNumOfRows()).orElse("1000");

        Map<String, String> params = Map.of(
            "ServiceKey", serviceKey,
            "MobileOS", "ETC",
            "MobileApp", "AppTest",
            "_type", "json",
            "keyword", keyword,
            "pageNo",pageNo,
            "numOfRows", numOfRows
        );

        String url = buildUrlWithParams(getCategoryUrl, params);

        return getListCompletableFuture(url);
    }

    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteAreaBasedList(TourAPIDto.TourRequest requestParam) {
        String getCategoryUrl = baseUrl + "areaBasedList1";
        long startTime = System.currentTimeMillis();
        String[] areaCodes = {"1","2","3","4","5","6","7","8","31","32","33","34","35","36","37","38","39"};
        String[] contentTypeIds = {"12","14","15","25","28","32","38","39"};
        String pageNo = Optional.ofNullable(requestParam.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestParam.getNumOfRows()).orElse("1000");
       /* int[] sigunguCodes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 99}; 같은 시군구 코드에 다른지역 표기가 있어 제외*/

        // stream(contentTypeIds) or Arrays.stream(areaCodes) 로 활용
        List<CompletableFuture<List<TourAPI>>> futures = Arrays.stream(areaCodes)
            .flatMap(areaCode -> Arrays.stream(contentTypeIds)
                .map(contentTypeId -> {
                    Map<String, String> params = Map.of(
                        "ServiceKey", serviceKey,
                        "MobileOS", "ETC",
                        "MobileApp", "AppTest",
                        "_type", "json",
                        "areaCode", areaCode,
                        "numOfRows", numOfRows,
                        "pageNo", pageNo,
                        "contentTypeId", contentTypeId
                    );

                    String url = buildUrlWithParams(getCategoryUrl, params);
                    return getListCompletableFuture(url);
                })
            ).collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.info("getTourAPIFromSiteAreaBasedList executed in " + duration + " ms");
                return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            });
    }

    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteAreaBasedListSingle(TourAPIDto.TourRequest requestParam) {
        String getCategoryUrl = baseUrl + "areaBasedList1";
        String pageNo = Optional.ofNullable(requestParam.getPageNo()).orElse("0");
        String numOfRows = Optional.ofNullable(requestParam.getNumOfRows()).orElse("10");
        String areaCode = Optional.ofNullable(requestParam.getAreaCode()).orElse("");
        String contentTypeId = Optional.ofNullable(requestParam.getContentTypeId()).orElse("");
        String sigunguCode = Optional.ofNullable(requestParam.getSigunguCode()).orElse("");

        Map<String, String> params = Map.of(
            "ServiceKey", serviceKey,
            "MobileOS", "ETC",
            "MobileApp", "AppTest",
            "_type", "json",
            "areaCode",areaCode,
            "contentTypeId",contentTypeId,
            "sigunguCode",sigunguCode,
            "pageNo", pageNo,
            "numOfRows", numOfRows
        );

        String url = buildUrlWithParams(getCategoryUrl, params);

        return getListCompletableFuture(url);
    }
    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteAreaCode(TourAPIDto.TourRequest requestParam) {
        String getCategoryUrl = baseUrl + "areaCode1";
        String areaCode = Optional.ofNullable(requestParam.getAreaCode()).orElse("");
        // areaCode 가 비어있으면 code(true) areaCode가 있으면 Sigungu(false)
        boolean codeOrSigungu = areaCode.isEmpty();
        // areaCode 가 비어있을 때 모든 특별시, 도를 검색 (true)
        // areaCode가 있을 때는 해당 areaCode의 sigungu를 검색 (false)

        Map<String, String> params = Map.of(
            "ServiceKey", serviceKey,
            "MobileOS", "ETC",
            "MobileApp", "AppTest",
            "_type", "json",
            "numOfRows","100",
            "areaCode",areaCode
        );

        String url = buildUrlWithParams(getCategoryUrl, params);

        return getListCompletableFutureForArea(url, areaCode, codeOrSigungu);
    }

    @NotNull
    private CompletableFuture<List<TourAPI>> getListCompletableFuture(String url) {
        // 공공데이터 request url에 맞춰 build된 url에 json형식으로 header 추가
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Content-type", "application/json")
            .build();

        //build된 url을 기반으로 공공데이터 비동기 방식으로 response 가져오기 -> Entity 저장
        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseJson = response.body().string();

//                    logger.info("Response JSON: " + responseJson); // Log the JSON response

                // items이 비어있을 때 Error Msg없이 emptyList 출력 - 이 코드가 없으면 error 발생
                if (responseJson.contains("\"items\": \"\"")) {
                    return Collections.emptyList();
                }

                TourAPIDto tourAPIDto = gson.fromJson(responseJson, TourAPIDto.class);

                List<TourAPI> tourAPIList = tourAPIDto.toEntityList();

                if (!tourAPIList.isEmpty()) {
                    tourAPIRepository.saveAll(tourAPIList);
                    return tourAPIList;
                }
            } catch (IOException | JsonSyntaxException e) {
                if (!e.getMessage().contains("\"items\": \"\",")) {
                    logger.error("JSON parsing error: " + e.getMessage());
                }
            }
            return Collections.emptyList();
        }, executorService);
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        params.forEach((key, value) -> urlBuilder.addQueryParameter(key, value));
        return urlBuilder.build().toString();
    }

    private CompletableFuture<Void> fetchAndUpdateTourAPIData(String url, TourAPI existingTourAPI) {
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Content-type", "application/json")
            .build();

        return CompletableFuture.runAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseJson = response.body().string();
                logger.debug("Response JSON: " + responseJson);

                // JSON 파싱 및 데이터 형식 검증
                TourAPIDto tourAPIDto = gson.fromJson(responseJson, TourAPIDto.class);

                TourAPIDto.TourResponse.Body.Items.Item item = tourAPIDto.getResponse()
                    .getBody()
                    .getItems()
                    .getItem()
                    .get(0);
                // 공공데이터에서 가져온 data를 기존 Entity Data(TourAPI)에 업데이트
                existingTourAPI.updateFromDtoAsync(item);
            } catch (IOException | JsonSyntaxException e) {
                logger.error("Error fetching data for URL: " + url, e);
                throw new CompletionException(e);
            }
        }, executorService);
    }

    @NotNull
    private CompletableFuture<List<TourAPI>> getListCompletableFutureForArea(String url,String areaCode,boolean codeOrSigungu) {
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Content-type", "application/json")
            .build();

        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseJson = response.body().string();

//                    logger.info("Response JSON: " + responseJson); // Log the JSON response

                // items이 비어있을 때 Error Msg없이 emptyList 출력 - 이 코드가 없으면 error 발생
                if (responseJson.contains("\"items\": \"\"")) {
                    return Collections.emptyList();
                }

                TourAPIDto tourAPIDto = gson.fromJson(responseJson, TourAPIDto.class);

                List<TourAPI> tourAPIList = tourAPIDto.toEntityListArea(areaCode,codeOrSigungu);

                if (!tourAPIList.isEmpty()) {
                    tourAPIRepository.saveAll(tourAPIList);
                    return tourAPIList;
                }

            } catch (IOException | JsonSyntaxException e) {
                if (!e.getMessage().contains("\"items\": \"\",")) {
                    logger.error("JSON parsing error: " + e.getMessage());
                }
            }
            return Collections.emptyList();
        }, executorService);
    }

}