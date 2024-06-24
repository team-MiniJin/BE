package com.minizin.travel.tour.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.service.TourInfoService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class: TourInfoController Project: travel Package: com.minizin.travel.tour.controller
 * <p>
 * Description: TourInfoController
 *
 * @author dong-hoshin
 * @date 6/16/24 13:49 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Slf4j
@RestController
@RequestMapping("/tour/info")
@RequiredArgsConstructor
public class TourInfoController {
    private final TourInfoService tourInfoService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @GetMapping("/areaCode1")
    public ResponseEntity<String> getTourDataByAreaCode(@ModelAttribute TourAPIDto.TourRequest requestUrl) throws IOException {
        log.info("Received request: {}", requestUrl);
        return processTourRequest(requestUrl, () -> {
            try {
                return tourInfoService.getTourDataByAreaCode(requestUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping("/areaBasedList1")
    public ResponseEntity<String> getTourDataByBasedList(@ModelAttribute TourAPIDto.TourRequest requestUrl) throws IOException {
        log.info("Received request: {}", requestUrl);
        try {
            return processTourRequest(requestUrl, () -> {
                try {
                    return tourInfoService.getTourDataByAreaBasedList(requestUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @GetMapping("/searchKeyword1")
    public ResponseEntity<String> getTourDataBySearchKeyword(@ModelAttribute TourAPIDto.TourRequest requestUrl) throws IOException {
        log.info("Received request: {}", requestUrl);
        try {
            return processTourRequest(requestUrl, () -> {
                try {
                    return tourInfoService.getTourDataSearchKeyword(requestUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    @GetMapping("/detailCommon1")
    public ResponseEntity<String> getTourDataByDetailCommon(@ModelAttribute TourAPIDto.TourRequest requestUrl) throws IOException {
        log.info("Received request: {}", requestUrl);
        try {
            return processTourRequest(requestUrl, () -> {
                try {
                    return tourInfoService.getTourDataByDetailCommon(requestUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            return handleException(e);
        }
    }

    private ResponseEntity<String> processTourRequest(TourAPIDto.TourRequest requestUrl, Supplier<TourAPIDto> tourDataSupplier) {
        try {
            if (requestUrl.getServiceKey() != null ) {
                TourAPIDto responseDto = tourDataSupplier.get();
                String jsonResponse = gson.toJson(responseDto);
                log.info(jsonResponse);
                return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
            } else {
                String errorResponse = "{\"successful\":false,\"redirect\":false}";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(errorResponse);
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private ResponseEntity<String> handleException(RuntimeException e) {
        String errorResponse;
        if (e.getCause() instanceof IOException) {
            errorResponse = "{\"successful\":false,\"error\":\"IOException occurred\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(errorResponse);
        } else {
            errorResponse = "{\"successful\":false,\"error\":\"Unexpected error occurred\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }
    }
}
