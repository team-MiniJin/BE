package com.minizin.travel.tour.domain.repository;

import com.minizin.travel.tour.domain.dto.TourAPIDto;
import com.minizin.travel.tour.domain.entity.TourAPI;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface TourAPIRepository extends JpaRepository<TourAPI, Long> {
    // TourAPI 에서 중복 값 없이(DISTINCT) Null 값 & 빈 값('') 제외하고 가져오기.
    @Query(value = "SELECT DISTINCT t.code, t.name, t.rnum FROM TourAPI t WHERE t.code IS NOT NULL AND t.code != ''")
    List<Object[]> findDistinctAreaCode();

}
