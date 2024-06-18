package com.minizin.travel.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode {

    AUTH_CODE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증코드 발송에 실패했습니다."),
    TEMPORARY_PASSWORD_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "임시 비밀번호 발송에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
