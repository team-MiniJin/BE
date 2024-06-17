package com.minizin.travel.tour.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;

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

        List<CompletableFuture<List<TourAPI>>> futures = Arrays.stream(areaCodes)
            .mapToObj(code -> {
                Map<String, String> params = Map.of(
                    "ServiceKey", serviceKey,
                    "MobileOS", "ETC",
                    "MobileApp", "AppTest",
                    "_type", "json",
                    "areaCode", String.valueOf(code)
                );

                String url = buildUrlWithParams(getCategoryUrl, params);
                return getListCompletableFuture(url);
            }).collect(Collectors.toList());

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
//                System.out.println("Response =" + response);
                String responseJson = response.body().string();
//                System.out.println(responseJson);
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
}