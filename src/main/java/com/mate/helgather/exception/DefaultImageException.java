package com.mate.helgather.exception;

import lombok.Getter;

@Getter
public class DefaultImageException extends Exception {
    ErrorCode errorCode = ErrorCode.DEFAULT_IMAGE_ERROR;
}
