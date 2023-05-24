package com.mate.helgather.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
