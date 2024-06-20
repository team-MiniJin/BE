package com.minizin.travel.global;

import com.minizin.travel.global.enums.ValidationErrorCode;
import com.minizin.travel.user.domain.dto.ErrorResponse;
import com.minizin.travel.user.domain.exception.CustomMailException;
import com.minizin.travel.user.domain.exception.CustomUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<?> handleCustomUserException(CustomUserException e) {
        return ResponseEntity.status(e.getUserErrorCode().getStatus())
                .body(new ErrorResponse(e.getUserErrorCode().getStatus(),
                        e.getUserErrorCode().getMessage())
                );
    }

    @ExceptionHandler(CustomMailException.class)
    public ResponseEntity<?> handleCustomMailException(CustomMailException e) {
        return ResponseEntity.status(e.getMailErrorCode().getStatus())
                .body(new ErrorResponse(e.getMailErrorCode().getStatus(),
                        e.getMailErrorCode().getMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationErrorResponse.FieldError> errors = e.getBindingResult().getFieldErrors()
                .stream().map(error -> new ValidationErrorResponse.FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse(ValidationErrorCode.INVALID_REQUEST.getStatus(),
                        ValidationErrorCode.INVALID_REQUEST.getMessage(), errors));
    }
}
