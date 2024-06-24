package com.minizin.travel.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.dto
 * <p>
 * Description: ChatRoomRequest
 *
 * @author JANG CHIHUN
 * @date 6/4/24 10:20 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatRoomRequest {

    private String roomName;

    private String roomPwd;

    private boolean secretChk;

    private int maxUserCnt;
}
