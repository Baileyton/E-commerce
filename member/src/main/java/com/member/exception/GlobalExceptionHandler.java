package com.member.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleUserException(MemberException e) {
        ErrorCode errorCode = e.getErrorCode();
        logger.error("UserException: {}", errorCode.getErrorMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorCode.getErrorMessage());
    }
}
