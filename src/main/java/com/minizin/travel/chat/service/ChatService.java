package com.minizin.travel.chat.service;

import static com.minizin.travel.chat.exception.ChatRoomException.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minizin.travel.chat.domain.ChatRoom;
import com.minizin.travel.chat.repository.ChatRoomRepository;
import com.minizin.travel.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.service
 * <p>
 * Description: ChatService
 *
 * @author JANG CHIHUN
 * @date 6/4/24 11:00 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public void plusUserCnt(String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);

        log.info("cnt {}", chatRoom.getUserCount());
        chatRoom.incrementUserCount();
    }

    @Transactional
    public void minusUserCnt(String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);

        chatRoom.decrementUserCount();;
    }

    public boolean chkRoomUserCnt(String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);

        return chatRoom.canAddUser();
    }


    private ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_ERROR_CODE));
    }
}
