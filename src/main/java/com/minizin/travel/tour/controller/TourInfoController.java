package com.minizin.travel.tour.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.service.TourInfoService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if (requestUrl.getServiceKey() != null && "0".equals(requestUrl.getServiceKey())) {
            TourAPIDto responseDto = tourInfoService.getTourDataByAreaCode();
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

    }
}
