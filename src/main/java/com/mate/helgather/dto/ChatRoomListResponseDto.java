package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class ChatRoomListResponseDto {
    String title; // 상대 유저 아이디
    String time;
    String image; // 유저 프로필 사진
    String preview; // 프리뷰
    Long chatId; // 채팅방 id

    public ChatRoomListResponseDto(String title, String nickname, String image, String time, String preview, Long chatId) {
        this.title = nickname + "님의 " + title;
        this.image = image;
        this.time = time;
        this.preview = preview;
        this.chatId = chatId;
    }
}
