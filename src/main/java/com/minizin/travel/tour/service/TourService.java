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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
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

        Map<String, String> params = new HashMap<>();
        params.put("ServiceKey", serviceKey); // 필수
        params.put("MobileOS", "ETC"); // 필수
        params.put("MobileApp", "AppTest"); // 필수
        params.put("keyword", "강원");
        params.put("_type", "json");

        String url = buildUrlWithParams(getCategoryUrl, params);

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

    public List<TourAPI> getTourAPIFromSiteAreaCode() {
        String getCategoryUrl = baseUrl + "areaCode1";

        Map<String, String> params = new HashMap<>();
        params.put("ServiceKey", serviceKey); // 필수
        params.put("MobileOS", "ETC"); // 필수
        params.put("MobileApp", "AppTest"); // 필수
        params.put("_type", "json");

        String url = buildUrlWithParams(getCategoryUrl, params);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Content-type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

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
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        params.forEach((key, value) -> urlBuilder.addQueryParameter(key, value));
        return urlBuilder.build().toString();
    }


    public List<TourAPI> getTourAPIFromSiteAreaBasedList() {
        int[] areaCode = {1, 2, 3, 4, 5, 6, 7, 8, 31, 32};
        StringBuilder sb = new StringBuilder();

        for (int i : areaCode) {
            try {
                StringBuilder urlBuilder = new StringBuilder(
                    "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList");
                urlBuilder.append("?");
                urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8")
                    + "=" + URLEncoder.encode(serviceKey, "UTF-8")); // 필수
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8")
                    + "=" + URLEncoder.encode("10", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8")
                    + "=" + URLEncoder.encode("1", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8")
                    + "=" + URLEncoder.encode("ETC", "UTF-8")); // 필수
                urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8")
                    + "=" + URLEncoder.encode("AppTest", "UTF-8")); // 필수
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8")
                    + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8")
                    + "=" + URLEncoder.encode("Y", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8")
                    + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(sb.toString());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TourAPIDto tourAPIDto = objectMapper.readValue(sb.toString(), TourAPIDto.class);
            List<TourAPI> tourAPIList = tourAPIDto.toEntityList();
            for (TourAPI tourAPI : tourAPIList) {
                tourAPIRepository.save(tourAPI);
            }

            return tourAPIList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}