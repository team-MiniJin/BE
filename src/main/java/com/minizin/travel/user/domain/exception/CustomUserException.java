package com.minizin.travel.user.domain.exception;

import com.minizin.travel.user.domain.enums.UserErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserException extends RuntimeException {
    private final UserErrorCode userErrorCode;
}
