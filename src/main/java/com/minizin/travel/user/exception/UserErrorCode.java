package com.minizin.travel.user.exception;


import com.minizin.travel.global.exception.ErrorCode;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.exception
 * <p>
 * Description: UserErrorCode
 *
 * @author JANG CHIHUN
 * @date 6/3/24 21:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
public record UserErrorCode(int status, String message) implements ErrorCode {
    public static final UserErrorCode USER_NOT_FOUND = new UserErrorCode(404, "User not found");
    public static final UserErrorCode INVALID_USER = new UserErrorCode(400, "Invalid user");
}
