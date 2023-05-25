package com.mate.helgather.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final int code;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

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
    public BaseResponse(T result) {
        this.isSuccess = true;
        this.code = 200;
        this.message = "성공입니다.";
        this.result = result;
    }

    // 넘겨받을 때
    public BaseResponse(boolean isSuccess, int code, String message, T result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
