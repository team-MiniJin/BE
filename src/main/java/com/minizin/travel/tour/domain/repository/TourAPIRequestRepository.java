package com.minizin.travel.tour.domain.repository;

import com.minizin.travel.tour.domain.entity.TourAPI;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Class: TourAPIRequestRepository Project: travel Package:
 * com.minizin.travel.tour.domain.repository
 * <p>
 * Description: TourAPIRequestRepository
 *
 * @author dong-hoshin
 * @date 6/3/24 20:00 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Repository
public interface TourAPIRequestRepository extends JpaRepository<TourAPI, Long> {
    Optional<TourAPI> findByRequestId(Long RequestId);

}
