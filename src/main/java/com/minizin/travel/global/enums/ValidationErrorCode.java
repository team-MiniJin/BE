package com.minizin.travel.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
