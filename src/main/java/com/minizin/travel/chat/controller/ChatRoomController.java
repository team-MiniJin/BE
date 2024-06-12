package com.minizin.travel.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.minizin.travel.chat.dto.ChatRoomRequest;
import com.minizin.travel.chat.dto.ChatRoomResponse;
import com.minizin.travel.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.controller
 * <p>
 * Description: ChatRoomController
 *
 * @author JANG CHIHUN
 * @date 6/4/24 14:20 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/createroom")
    public ChatRoomResponse createRoom(@RequestParam ChatRoomRequest request) {
        ChatRoomResponse room = chatRoomService.createChatRoom(request);
        log.info("CREATE Chat Room {}", room);
        return room;
    }

    @GetMapping("/rooms")
    public List<ChatRoomResponse> getChatRoom() {
        List<ChatRoomResponse> chatRooms = chatRoomService.findAllRoom();
        log.info("SHOW ALL ChatList {}", chatRooms);

        return chatRooms;
    }

    // 채팅방 입장 화면
    @GetMapping("/room/{roomId}")
    public ChatRoomResponse roomDetail(@PathVariable String roomId) {
        log.info("roomId {}", roomId);
        return chatRoomService.findRoomById(roomId);
    }

    @ResponseBody
    @PostMapping("/chat/confirmPwd/{roomId}")
    public boolean confirmPwd(@PathVariable String roomId, @RequestParam String roomPwd) {
        // 넘어온 roomId 와 roomPwd 를 이용해서 비밀번호 찾기
        // 찾아서 입력받은 roomPwd 와 room pwd 와 비교해서 맞으면 true, 아니면  false
        return chatRoomService.confirmPwd(roomId, roomPwd);
    }


    // 채팅방 삭제
    @GetMapping("/caht/delRoom/{roomId}")
    public void delChatRoom(@PathVariable String roomId) {
        try {
            // roomId 기준으로 채팅방 삭제, 해당 채팅방 안에 있는 사진 삭제
            chatRoomService.delChatRoom(roomId);
        } catch (Exception e) {
            log.error("Error deleting chat room: {}", e.getMessage());
        }
    }

}
