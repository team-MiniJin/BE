package com.minizin.travel.user.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final String details;
}
