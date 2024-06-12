package com.minizin.travel.chat.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class: BaseEntity Project: com.minizin.travel.chat.domain
 * <p>
 * Description: ChatRoom
 *
 * @author JANG CHIHUN
 * @date 6/4/24 10:00 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private String roomName;
    private String roomPwd;
    private boolean secretChk;
    private int userCount;
    private int maxUserCnt;

    @ElementCollection
    @CollectionTable(name = "chat_room_user_list",
            joinColumns = @JoinColumn(name = "chat_room_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "user_name")
    private Map<String, String> userList = new HashMap<>();


    public static ChatRoom create(String roomName, String roomPwd, boolean secretChk, int maxUserCnt) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .roomPwd(roomPwd)
                .secretChk(secretChk)
                .userCount(0)
                .maxUserCnt(maxUserCnt)
                .userList(new HashMap<>())
                .build();
    }
    public void incrementUserCount() {
        this.userCount++;
    }

    public void decrementUserCount() {
        this.userCount--;
    }

    public void addUserList(Map<String, String> userList) {
        this.userList = userList;
    }

    // 최대 사용자 수 확인
    public boolean canAddUser() {
        return this.userCount + 1 <= this.maxUserCnt;
    }
}
