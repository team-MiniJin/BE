package com.minizin.travel.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    EMAIL_UN_REGISTERED(HttpStatus.BAD_REQUEST, "등록되지 않은 이메일입니다."),
    USER_EMAIL_UN_MATCHED(HttpStatus.BAD_REQUEST, "사용자에게 등록된 이메일이 아닙니다."),
    PASSWORD_UN_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
