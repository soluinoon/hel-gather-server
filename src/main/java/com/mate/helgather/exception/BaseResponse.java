package com.mate.helgather.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

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

    // 실패했을 때 (에러코드이용)
    public BaseResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BaseResponse(BindingResult bindingResult) {
        this.isSuccess = false;
        this.code = ErrorCode.FORMAT_ERROR.getCode();
        this.message = bindingResult.getFieldError().getDefaultMessage();
    }

    // 성공했을 때
    public BaseResponse(Object result) {
        this.isSuccess = true;
        this.code = 200;
        this.message = "성공입니다.";
        this.result = result;
    }

    // 넘겨받을 때
    public BaseResponse(boolean isSuccess, int code, String message, Object result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
