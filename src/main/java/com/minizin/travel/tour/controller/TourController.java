package com.minizin.travel.tour.controller;

import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import com.minizin.travel.tour.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class: TourController Project: travel Package: com.minizin.travel.tour.controller
 * <p>
 * Description: TourController
 *
 * @author dong-hoshin
 * @date 6/7/24 02:58 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Tag(name = "Tour Controller")
@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @Operation(summary = "Get areaBasedList", description = "Retrieve a specific areaBasedList by its ID")
    @GetMapping("/areaBasedList")
    public ResponseEntity<TourAPIDto> getAPITourDataAreaBasedList() {
        return ResponseEntity.ok(tourService.getTourAPIFromSite());
    }

    @Operation(summary = "Get areaCode", description = "Retrieve a specific areaCode")
    @GetMapping("/areaCode")
    public ResponseEntity<List<TourAPI>> getAPITourDataAreaCode() {
        return ResponseEntity.ok(tourService.getTourAPIFromSiteAreaCode());
    }

}