package com.minizin.travel.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
