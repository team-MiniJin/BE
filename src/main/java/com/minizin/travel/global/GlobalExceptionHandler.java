package com.minizin.travel.global;

import com.minizin.travel.global.enums.ValidationErrorCode;
import com.minizin.travel.user.domain.dto.ErrorResponse;
import com.minizin.travel.user.domain.exception.CustomMailException;
import com.minizin.travel.user.domain.exception.CustomUserException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Database error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<String> handleJDBCConnectionException(JDBCConnectionException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Database connection error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLGrammarException.class)
    public ResponseEntity<String> handleSQLGrammarException(SQLGrammarException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("SQL syntax error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Database constraint violation: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Invalid argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
