package com.mate.helgather.exception;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse> handleBaseException(BaseException e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(e.getErrorCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<BaseResponse> handleAmzonS3Exception(AmazonS3Exception e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(ErrorCode.S3_CONNECT_ERROR);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(e.getBindingResult());
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(S3NoPathException.class)
    public ResponseEntity<BaseResponse> handleIOException(S3NoPathException e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(e.getErrorCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DefaultImageException.class)
    public ResponseEntity<BaseResponse> handleDefaultImageException(DefaultImageException e) {
        e.printStackTrace();
        BaseResponse baseResponse = new BaseResponse(e.getErrorCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }
}
