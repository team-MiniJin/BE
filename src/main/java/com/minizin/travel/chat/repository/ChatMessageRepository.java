package com.minizin.travel.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minizin.travel.chat.domain.ChatMessage;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.repository
 * <p>
 * Description: ChatMessageRepository
 *
 * @author JANG CHIHUN
 * @date 6/17/24 11:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	List<ChatMessage> findByRoomId(String roomId);

	void deleteByRoomIdAndId(String roomId, Long id);
}
