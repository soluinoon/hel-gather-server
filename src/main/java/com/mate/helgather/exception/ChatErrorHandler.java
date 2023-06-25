package com.mate.helgather.exception;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable convertedEx = convertException(ex);
        if (convertedEx instanceof BaseException) {
            return prepareErrorMessage((BaseException) convertedEx);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> prepareErrorMessage(BaseException exception) {
        String code = String.valueOf(exception.getErrorCode().getCode());

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setNativeHeader("code", code);
        accessor.setMessage(String.valueOf(exception.getErrorCode().getMessage()));
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage("error".getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }

    private Throwable convertException(Throwable ex) {
        if (ex instanceof MessageDeliveryException) {
            return ex.getCause();
        } else {
            return ex;
        }
    }

}
