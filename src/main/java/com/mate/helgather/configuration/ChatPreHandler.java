package com.mate.helgather.configuration;

import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String BEARER_PREFIX = "[Bearer";

    /**
     * STOMP JWT 검증 핸들러
     * 현재 [CONNECT]만 검증하도록 되어있습니다.
     * 나중에 프론트 엔드와 SUBSCRIBE, PUBLISH 까지 검증을 해야하는지에 대한
     * 논의가 필요합니다.
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("pre send 호출");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = headerAccessor.getCommand();
        if (!command.equals(StompCommand.CONNECT)) {
            return message;
        }
        String authorization = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        if (authorization == null || authorization.equals("null") || !authorization.startsWith(BEARER_PREFIX)) {
            throw new BaseException(ErrorCode.INVALID_JWT_TOKEN);
        }
        // 토큰 만들기 authorization -> token
        String token = authorization.substring(8, authorization.length() - 1);
        // 검증
        jwtTokenProvider.validateTokenAtStomp(token);
        log.info("검증 통과");
        return message;
    }

    private void printStompHead(StompHeaderAccessor headerAccessor) {
        Map<String, Object> headers = headerAccessor.toMap();
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            Object headerValue = entry.getValue();
            log.info("Header - {}: {}", headerName, headerValue);
        }
    }
}
