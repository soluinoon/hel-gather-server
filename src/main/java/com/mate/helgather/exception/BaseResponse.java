package com.mate.helgather.exception;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;

@Getter
public class BaseResponse {

    private boolean isSuccess;
    private int code;

    private String message;
    private Object result;

    // 실패했을 때
    public BaseResponse(int code, String message) {
        this.isSuccess = false;
        this.code = code;
        this.message = message;
    }

    // 성공했을 때
    public BaseResponse(int code, String message, Object result) {
        this.isSuccess = true;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    //
    public BaseResponse(boolean isSuccess, int code, String message, Object result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
