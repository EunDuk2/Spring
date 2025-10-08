package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.service.ChatService;
import com.example.chatserver.chat.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;
    private final RedisPubSubService redisPubSubService;

    // 방법1. MessageMapping(수신)과 SendTo(topic에 메시지 전달) 한꺼번에 처리
    // 아래 한 세트가 브로커 역할을 해준다. (메시지 받고, 전달까지)
//    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId형태로 메시지를 발생 시 MessageMapping 수신
//    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
//    // DestinationVariable : @MessageMapping 어노테이션 정의된 Websocket Controller내에서만 사용
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        System.out.println(message);
//
//        return message; // 리턴해주면 @SendTo에서 이 메시지를 해당 토픽에 발행
//    }

    // 방법2. MessageMapping 어노테이션만 활용
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto dto) throws JsonProcessingException {
        System.out.println(dto.getMessage());

        chatService.saveMessage(roomId, dto);

        // SendTo 대신 코드로
//        messageTemplate.convertAndSend("/topic/"+roomId, dto);

        dto.setRoomId(roomId);

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(dto);
        redisPubSubService.publish("chat", message);
    }
}
