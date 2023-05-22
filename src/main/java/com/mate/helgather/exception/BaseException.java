package com.mate.helgather.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception{
    ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
