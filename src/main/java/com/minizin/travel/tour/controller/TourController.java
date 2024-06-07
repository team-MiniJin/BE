package com.minizin.travel.tour.controller;

import com.minizin.travel.tour.domain.entity.TourAPIResponse;
import com.minizin.travel.tour.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "tour Controller")
@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
    @Operation(summary = "Get Wiki Page by ID", description = "Retrieve a specific WikiPage by its ID")
    @GetMapping("/areaBasedList")
    public ResponseEntity<TourAPIResponse> getAPITourData() {
        return ResponseEntity.ok(tourService.getTourAPIFromSite());
    }
}
