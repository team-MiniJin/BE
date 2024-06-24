package com.minizin.travel.s3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minizin.travel.s3.domain.ChatFile;

/**
 * Class: BaseEntity Project: package com.minizin.travel.s3.repository
 * <p>
 * Description: ChatFileRepository
 *
 * @author JANG CHIHUN
 * @date 6/17/24 11:51 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

public interface ChatFileRepository extends JpaRepository<ChatFile, Long> {
	List<ChatFile> findByRoomId(String roomId);
	void deleteByRoomIdAndId(String roomId, Long id);
}
