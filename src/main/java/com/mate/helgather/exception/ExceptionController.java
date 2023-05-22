package com.mate.helgather.exception;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<BaseResponse> NoMemberException(Exception e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(401, "존재하지 않는 유저 입니다.");
        return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
    }
}
