package com.mate.helgather.dto;

public class ChatRoomListResponse {
    // 상대 유저 아이디
    String id;
    String time;
    // 유저 프로필 사진
    int profile;
    // 프리뷰
    String preview;
    // 채팅방 id
    Long chatId;

    public ChatRoomListResponse(String id, String time, String preview, Long chatId) {
        this.id = id;
        this.profile = 0;
        this.time = time;
        this.preview = preview;
        this.chatId = chatId;
    }
}
