package com.minizin.travel.chat.exception;

import com.minizin.travel.global.exception.ErrorCode;
import com.minizin.travel.user.exception.UserErrorCode;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.dto
 * <p>
 * Description: ChatRoomException
 *
 * @author JANG CHIHUN
 * @date 6/4/24 10:15 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

public record ChatRoomException(int status,
                                String message) implements ErrorCode {
    public static final UserErrorCode CHAT_ROOM_ERROR_CODE = new UserErrorCode(404, "Room not found");

}
