package com.minizin.travel.global;

import com.minizin.travel.global.enums.ValidationErrorCode;
import com.minizin.travel.user.domain.dto.ErrorResponse;
import com.minizin.travel.user.domain.exception.CustomUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<?> handleCustomUserException(CustomUserException e) {
        return ResponseEntity.status(e.getUserErrorCode().getStatus())
                .body(new ErrorResponse(e.getUserErrorCode().getStatus(),
                        e.getUserErrorCode().getMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ValidationErrorCode.INVALID_REQUEST.getStatus(),
                        ValidationErrorCode.INVALID_REQUEST.getMessage()));
    }
}
