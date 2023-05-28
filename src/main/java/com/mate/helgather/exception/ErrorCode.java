package com.mate.helgather.exception;

import lombok.Getter;

public enum ErrorCode {
    /**
     *  3000번대는 형식 오류
     *  4000번대는 존재, 중복 유무. 즉 레포지터리를 검사해야 하는 오류
     *  5000번대는 서버, 데이터베이스 연결 오류
     */
    FORMAT_ERROR(3001, "포맷 에러 입니다."),
    PHONE_PATTERN_ERROR(3002, "올바른 전화번호 형식으로 입력해주세요."),
    PASSWORD_PATTERN_ERROR(3003, "비밀번호는 소문자, 대문자, 숫자, 특수문자를 각각 하나 이상 포함한 8자 이상이어야 합니다."),
    NO_INPUT_USERNAME(3004, "이름을 입력해주세요."),
    NO_INPUT_PHONE(3005, "전화번호를 입력해주세요."),
    NO_INPUT_NICKNAME(3006, "닉네임을 입력해주세요."),
    NO_INPUT_PASSWORD(3007, "비밀번호를 입력해주세요."),
    NO_INPUT_BIRTH_YEAR(3008, "생일년도를 입력해주세요."),
    NO_INPUT_BIRTH_MONTH(3009, "생일달을 입력해주세요."),
    NO_INPUT_BIRTH_DAY(3010, "생일을 입력해주세요."),
    NO_SUCH_MEMBER_ERROR(4001, "존재하지 않는 멤버 입니다."),
    NO_SUCH_CHATROOM_ERROR(4002, "존재하지 않는 채팅방 입니다."),
    EXIST_NICKNAME_ERROR(4003, "이미 존재하는 닉네임입니다."),
    EXIST_PHONE_ERROR(4004, "이미 존재하는 전화번호입니다."),
    PASSWORD_CORRECT_ERROR(4005, "비밀번호가 일치하지 않습니다."),
    NO_SUCH_CATEGORY_ERROR(4006, "카테고리가 존재하지 않습니다."),
    MEMBER_NOT_EXIST_IN_ROOM(4007, "채팅방에 존재하지 않는 멤버입니다."),
    EXIST_MEMBER_PROFILE(4008, "이미 프로필 등록된 회원입니다."),
    NO_SUCH_SUB_LOCATION_ERROR(4009, "존재하지 않는 지역 입니다."),
    NO_SUCH_LOCATION_ERROR(4010, "존재하지 않는 상세 지역 입니다."),
    NO_SUCH_RECRUITMENT_ERROR(4011, "존재하지 않는 게시물 입니다."),
    S3_CONNECT_ERROR(5001, "S3 오류입니다."),
    S3_NO_PATH_ERROR(5002, "S3 경로 디렉토리가 있는지 확인해주세요."),
    DEFAULT_IMAGE_ERROR(5003, "기본 이미지가 없습니다.");


    @Getter
    private int code;
    @Getter
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
