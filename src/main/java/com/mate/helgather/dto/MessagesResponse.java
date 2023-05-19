package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class MessagesResponse {
    Long userId; //유저 아이디
    int userProfile; //유저 프로필 사진
    String message; //메세지
    String time; // 언제
    boolean isFirst;

    public MessagesResponse(Long userId, int userProfile, String message, String time, boolean isFirst) {
        this.userId = userId;
        this.userProfile = userProfile;
        this.message = message;
        this.time = time;
        this.isFirst = isFirst;
    }
}
