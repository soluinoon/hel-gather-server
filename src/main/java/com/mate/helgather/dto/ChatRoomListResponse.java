package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class ChatRoomListResponse {
    String id; // 상대 유저 아이디
    String time;
    int profile; // 유저 프로필 사진
    String preview; // 프리뷰
    Long chatId; // 채팅방 id

    public ChatRoomListResponse(String id, String time, String preview, Long chatId) {
        this.id = id;
        this.profile = 0;
        this.time = time;
        this.preview = preview;
        this.chatId = chatId;
    }
}
