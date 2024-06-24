package com.minizin.travel.chat.service;

import static com.minizin.travel.chat.exception.ChatRoomException.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minizin.travel.chat.domain.ChatRoom;
import com.minizin.travel.chat.dto.ChatRoomRequest;
import com.minizin.travel.chat.dto.ChatRoomResponse;
import com.minizin.travel.chat.repository.ChatRoomRepository;
import com.minizin.travel.global.exception.CustomException;
import com.minizin.travel.s3.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.service
 * <p>
 * Description: ChatRoomService
 *
 * @author JANG CHIHUN
 * @date 6/5/24 10:40 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final FileService fileService;

    // roomName 로 채팅방 만들기
    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest request) {
        ChatRoom room = ChatRoomResponse.toEntity(request);

        chatRoomRepository.save(room);

        return ChatRoomResponse.of(room);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllRoom() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        Collections.reverse(chatRooms);

        return chatRooms.stream()
                .map(ChatRoomResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse findRoomById(String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);

        return ChatRoomResponse.of(chatRoom);
    }


    @Transactional(readOnly = true)
    // 특정 채팅방에서 사용자 UUID로 사용자 이름 조회
    public String findUsernameByRoomIdAndUserUUID(String roomId, String userUUID) {
        ChatRoom chatRoom = getChatRoom(roomId);

        return chatRoom.getUserList().get(userUUID);
    }

    @Transactional(readOnly = true)
    // 특정 채팅방의 전체 사용자 리스트 조회
    public List<String> getUserList(String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);

        return new ArrayList<>(chatRoom.getUserList().values());
    }

    @Transactional
    public String addUser(String roomId, String userName) {
        ChatRoom chatRoom = getChatRoom(roomId);
        String userUUID = UUID.randomUUID().toString();

        Map<String, String> userList = chatRoom.getUserList();

        if (userList == null) {
            userList = new HashMap<>();
            chatRoom.addUserList(userList);
        }

        userList.put(userUUID, userName);

        return userUUID;
    }

    @Transactional
    // 특정 채팅방에서 사용자 UUID로 사용자 삭제
    public void delUser(String roomId, String userUUID) {
        ChatRoom chatRoom = getChatRoom(roomId);

        // 사용자 목록에서 사용자 제거
        Map<String, String> userList = chatRoom.getUserList();
        if (userList != null && userList.containsKey(userUUID)) {
            userList.remove(userUUID);
        }
    }

    // 채팅방 삭제

    @Transactional
    public void delChatRoom(String roomId) {
        try {
            // 채팅방 조회
            ChatRoom chatRoom = getChatRoom(roomId);

            // 채팅방 삭제
            chatRoomRepository.delete(chatRoom);

            // 채팅방 안에 있는 파일 삭제
            fileService.deleteFileDir(roomId);

            log.info("삭제 완료 roomId : {}", roomId);
        } catch (Exception e) {
            log.error("채팅방 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }
    @Transactional(readOnly = true)
    public String isDuplicateName(String roomId, String username) {
        ChatRoom chatRoom = getChatRoom(roomId);
        String tmp = username;

        // 만약 userName 이 중복이라면 랜덤한 숫자를 붙임
        // 이때 랜덤한 숫자를 붙였을 때 getUserList 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
        while (chatRoom.getUserList().containsValue(tmp)) {
            int ranNum = (int) (Math.random() * 100) + 1;
            tmp = username + ranNum;
        }
        return tmp;
    }

    // 채팅방 비밀번호 조회
    @Transactional(readOnly = true)
    public boolean confirmPwd(String roomId, String roomPwd) {
        ChatRoom chatRoom = getChatRoom(roomId);

        return roomPwd.equals(chatRoom.getRoomPwd());
    }

    private ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_ERROR_CODE));
    }
}
