package com.minizin.travel.tour.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.domain.repository.TourAPIRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import org.springframework.web.client.RestTemplate;

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
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl = "https://apis.data.go.kr/B551011/KorService1/";
    private final TourAPIRepository tourAPIRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    Logger logger = LoggerFactory.getLogger(getClass());

    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteDetailCommon() {
        String getCategoryUrl = baseUrl + "detailCommon1";

        long startTime = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(2, 1000);
        Page<TourAPI> tourAPIPage = tourAPIRepository.findAll(pageRequest);

        List<CompletableFuture<TourAPI>> futures = tourAPIPage.stream()
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
                    Map.entry("numOfRows", "100"),
                    Map.entry("pageNo", "1")
                );

                String url = buildUrlWithParams(getCategoryUrl, params);
                return getSingleCompletableFuture(url, tourAPI);
            })
            .map(future -> future.exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            }))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                // Calculate and log the elapsed time
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                logger.info("getTourAPIFromSiteDetailCommon executed in " + duration + " ms");

                return futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            });
    }
    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteSearchKeyword() {
        String getCategoryUrl = baseUrl + "searchKeyword1";

        Map<String, String> params = Map.of(
            "ServiceKey", serviceKey,
            "MobileOS", "ETC",
            "MobileApp", "AppTest",
            "_type", "json",
            "keyword", "강원"
        );

        String url = buildUrlWithParams(getCategoryUrl, params);

        return getListCompletableFuture(url);
    }

    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteAreaBasedList() {
        String getCategoryUrl = baseUrl + "areaBasedList1";
        int[] areaCodes = {1, 2, 3, 4, 5, 6, 7, 8, 31, 32};
        int[] contentTypeIds = {12,14,15,25,28,32,38,39};
       /* int[] sigunguCodes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 99}; 같은 시군구 코드에 다른지역 표기가 있어 제외*/

        // stream(contentTypeIds) or Arrays.stream(areaCodes) 로 활용
        List<CompletableFuture<List<TourAPI>>> futures = Arrays.stream(areaCodes)
            .boxed()
            .flatMap(areaCode -> Arrays.stream(contentTypeIds)
                .mapToObj(contentTypeId -> {
                    Map<String, String> params = Map.of(
                        "ServiceKey", serviceKey,
                        "MobileOS", "ETC",
                        "MobileApp", "AppTest",
                        "_type", "json",
                        "areaCode", String.valueOf(areaCode),
                        "numOfRows", "50",
                        "pageNo", "1",
                        "contentTypeId", String.valueOf(contentTypeId)
                    );

                    String url = buildUrlWithParams(getCategoryUrl, params);
                    return getListCompletableFuture(url);
                })
            ).collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList())
            );
    }
    public CompletableFuture<List<TourAPI>> getTourAPIFromSiteAreaCode() {
        String getCategoryUrl = baseUrl + "areaCode1";

        Map<String, String> params = Map.of(
            "ServiceKey", serviceKey,
            "MobileOS", "ETC",
            "MobileApp", "AppTest",
            "_type", "json"
        );

        String url = buildUrlWithParams(getCategoryUrl, params);

        return getListCompletableFuture(url);
    }

    @NotNull
    private CompletableFuture<List<TourAPI>> getListCompletableFuture(String url) {
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
                System.out.println("Response =" + response);
                String responseJson = response.body().string();
                System.out.println(responseJson);
                TourAPIDto tourAPIDto = gson.fromJson(responseJson, TourAPIDto.class);

                List<TourAPI> tourAPIList = tourAPIDto.toEntityList();
                for (TourAPI tourAPI : tourAPIList) {
                    tourAPIRepository.save(tourAPI);
                }

                return tourAPIList;
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }, executorService);
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        params.forEach((key, value) -> urlBuilder.addQueryParameter(key, value));
        return urlBuilder.build().toString();
    }

    @NotNull
    private CompletableFuture<TourAPI> getSingleCompletableFuture(String url, TourAPI existingTourAPI) {
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
                TourAPIDto tourAPIDto = gson.fromJson(responseJson, TourAPIDto.class);
                TourAPIDto.TourResponse.Body.Items.Item item = tourAPIDto.getResponse()
                    .getBody()
                    .getItems()
                    .getItem()
                    .get(0);
                // Use the asynchronous update method
                CompletableFuture<Void> updateFuture = existingTourAPI.updateFromDtoAsync(item);

                // Wait for the update to complete before saving to the repository
                updateFuture.join();
                tourAPIRepository.save(existingTourAPI);

                return existingTourAPI;
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }, executorService);
    }

}