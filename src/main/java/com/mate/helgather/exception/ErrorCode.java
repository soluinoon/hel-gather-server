package com.mate.helgather.exception;

import lombok.Getter;

public enum ErrorCode {
    /**
     *  3000번대는 형식 오류
     *  4000번대는 존재, 중복 유무. 즉 레포지터리를 검사해야 하는 오류
     *  5000번대는 서버, 데이터베이스 연결 오류
     */
    FORMAT_ERROR(3001, "포맷 에러 입니다."),
    NO_SUCH_MEMBER_ERROR(4001, "존재하지 않는 멤버 입니다."),
    NO_SUCH_CHATROOM_ERROR(4002, "존재하지 않는 채팅방 입니다."),
    ;
    @Getter
    private int code;
    @Getter
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
