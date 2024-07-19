package com.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입 되어 있는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}