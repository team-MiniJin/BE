package com.minizin.travel.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minizin.travel.chat.domain.ChatRoom;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.repository;
 * <p>
 * Description: ChatRoomRepository
 *
 * @author JANG CHIHUN
 * @date 6/4/24 10:00 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);
}
