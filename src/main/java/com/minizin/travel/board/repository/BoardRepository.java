package com.minizin.travel.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minizin.travel.board.entity.Board;

/**
 * Class: BaseEntity Project: package com.minizin.travel.board.repository
 * <p>
 * Description: BoardRepository
 *
 * @author JANG CHIHUN
 * @date 6/17/24 10:15 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
}
