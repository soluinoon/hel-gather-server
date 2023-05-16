package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDto {
    private long roomId; // 방 번호
    private long userId; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 시간은 프론트에서 담아서 보내줄
    private boolean isFirst;
    private int userProfile;
}
