package com.minizin.travel.tour.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class: TourController Project: travel Package: com.minizin.travel.tour.controller
 * <p>
 * Description: TourController
 *
 * @author dong-hoshin
 * @date 6/8/24 03:35 Copyright (c) 2024 miniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
//@Tag(name = "Tour Controller")
@Slf4j
@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
    Logger logger = LoggerFactory.getLogger(getClass());
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Operation(summary = "Get detailCommon", description = "Retrieve a specific detailCommon ")
    @GetMapping("/detailCommon")
    public CompletableFuture<ResponseEntity<String>> getAPITourDataDetailCommon(@ModelAttribute TourAPIDto.TourRequest requestParam) {
        return tourService.getTourAPIFromSiteDetailCommon(requestParam)
            .thenApply(response -> {
                logger.info(response);  // 성공 메시지를 로그에 출력
                return ResponseEntity.ok(response);
            });
    }

    @Operation(summary = "Get areaBasedList", description = "Retrieve a specific areaBasedList ")
    @GetMapping("/areaBasedList")
    public CompletableFuture<ResponseEntity<List<TourAPI>>> getAPITourDataAreaBasedList(@ModelAttribute TourAPIDto.TourRequest requestParam) {
        return createResponseEntity(tourService.getTourAPIFromSiteAreaBasedList(requestParam));
    }
    @Operation(summary = "Get areaBasedListSingle", description = "Retrieve a specific Single areaBasedList with batch")
    @GetMapping("/areaBasedListSingle")
    public CompletableFuture<ResponseEntity<List<TourAPI>>> getAPITourDataAreaBasedListSingle(@ModelAttribute TourAPIDto.TourRequest requestParam) {
        return createResponseEntity(tourService.getTourAPIFromSiteAreaBasedListSingle(requestParam));
    }


    @Operation(summary = "Get areaCode", description = "Retrieve a specific areaBasedList by its ID")
    @GetMapping("/areacode")
    public CompletableFuture<ResponseEntity<List<TourAPI>>> getAPITourDataAreaCode(@ModelAttribute TourAPIDto.TourRequest requestParam) {
        return createResponseEntity(tourService.getTourAPIFromSiteAreaCode(requestParam));
    }

    @Operation(summary = "Get searchkeyword", description = "Retrieve a specific searchkeyword by keyword")
    @GetMapping("/searchkeyword")
    public CompletableFuture<ResponseEntity<List<TourAPI>>> getTourAPIFromSiteSearchKeyword(@ModelAttribute TourAPIDto.TourRequest requestParam) {
        return createResponseEntity(tourService.getTourAPIFromSiteSearchKeyword(requestParam));
    }

    private CompletableFuture<ResponseEntity<List<TourAPI>>> createResponseEntity(CompletableFuture<List<TourAPI>> future) {
        return future.thenApply(ResponseEntity::ok)
            .exceptionally(throwable -> ResponseEntity.status(500).build());
    }

}