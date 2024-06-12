package com.minizin.travel.chat.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.minizin.travel.chat.domain.ChatRoom;

import lombok.Builder;


/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.dto
 * <p>
 * Description: ChatRoomResponse
 *
 * @author JANG CHIHUN
 * @date 6/4/24 10:20 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Builder
public record ChatRoomResponse(String roomId, String roomName,
                               boolean secretChk, int userCount, int maxUserCnt,
                               Map<String, String> userList) {

    public static ChatRoomResponse of(ChatRoom chatRoom) {
        ChatRoomResponse response = ChatRoomResponse.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .secretChk(chatRoom.isSecretChk())
                .userCount(chatRoom.getUserCount())
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .userList(chatRoom.getUserList())
                .build();

        return response;
    }

    // DTO를 엔티티로 변환하는 메서드
    public static ChatRoom toEntity(ChatRoomRequest chatRoomRequest) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(chatRoomRequest.getRoomName())
                .roomPwd(chatRoomRequest.getRoomPwd())
                .secretChk(chatRoomRequest.isSecretChk())
                .userCount(0)
                .maxUserCnt(chatRoomRequest.getMaxUserCnt())
                .userList(new HashMap<>())
                .build();
    }

}
