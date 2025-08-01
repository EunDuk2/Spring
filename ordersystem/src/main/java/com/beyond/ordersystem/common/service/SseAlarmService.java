package com.beyond.ordersystem.common.service;

import com.beyond.ordersystem.common.dto.SseMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class SseAlarmService {
    private final SseEmitterRegistry sseEmitterRegistry;

    // 특정 사용자에게 message 발송
    // productId를 커스텀 할 수 있음
    public void publishMessage(String receiver, String sender, Long orderingId) {
        SseMessageDto dto = SseMessageDto.builder()
                .sender(sender)
                .receiver(receiver)
                .orderingId(orderingId)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String data = null;
        try {
            data = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // emitter 객체를 통해 메시지 전송
        SseEmitter sseEmitter = sseEmitterRegistry.getEmitter(receiver);
        try {
            sseEmitter.send(SseEmitter.event().name("ordered").data(data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 사용자가 로그아웃(새로고침)후에 다시 화면에 들어왔을 때, 알림 메시지가 남아있으려면 DB에 추가적인 저장 필요

    }
}
