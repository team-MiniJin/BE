package com.minizin.travel.tour.controller;

import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.service.TourService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

//    @Operation(summary = "Get areaBasedList", description = "Retrieve a specific areaBasedList by its ID")
    @GetMapping("/areaBasedList")
    public ResponseEntity<List<TourAPI>> getAPITourDataAreaBasedList() {
        return ResponseEntity.ok(tourService.getTourAPIFromSiteAreaBasedList());
    }

//    @Operation(summary = "Get areaCode", description = "Retrieve a specific areaBasedList by its ID")
    @GetMapping("/areacode")
    public ResponseEntity<List<TourAPI>> getAPITourDataAreaCode() {
        return ResponseEntity.ok(tourService.getTourAPIFromSiteAreaCode());
    }

}