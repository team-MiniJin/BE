package com.minizin.travel.user.domain.exception;

import com.minizin.travel.user.domain.enums.MailErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomMailException extends RuntimeException {
    private final MailErrorCode mailErrorCode;
}
