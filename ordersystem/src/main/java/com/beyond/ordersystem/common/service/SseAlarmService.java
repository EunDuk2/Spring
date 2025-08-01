package com.beyond.ordersystem.common.service;

import com.beyond.ordersystem.common.dto.SseMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SseAlarmService {
    // SseEmitter 연결된 사용자 정보(ip, mac address 정보 등)를 의미
    private Map<String, SseEmitter> emitterMap = new HashMap<>();


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
        SseEmitter sseEmitter = emitterMap.get(receiver);
        try {
            sseEmitter.send(SseEmitter.event().name("ordered").data(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addSseEmitter(String email, SseEmitter sseEmitter) {
        emitterMap.put(email, sseEmitter);
        System.out.println("eunsung111: " + emitterMap);
    }

}
