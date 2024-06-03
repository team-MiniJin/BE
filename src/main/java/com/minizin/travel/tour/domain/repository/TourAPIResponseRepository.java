package com.minizin.travel.tour.domain.repository;

import com.minizin.travel.tour.domain.entity.TourAPIResponse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Class: TourAPIResponseRepository Project: travel Package:
 * com.minizin.travel.tour.domain.repository
 * <p>
 * Description: TourAPIResponseRepository
 *
 * @author dong-hoshin
 * @date 6/3/24 20:03 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/lyckabc">GitHub Repository</a>
 */
@Repository
public interface TourAPIResponseRepository extends JpaRepository<TourAPIResponse, Long> {
    Optional<TourAPIResponse> findByResponseId(Long ResponseId);
}
