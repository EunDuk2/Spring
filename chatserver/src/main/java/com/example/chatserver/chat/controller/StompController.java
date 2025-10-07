package com.example.chatserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    // 아래 한 세트가 브로커 역할을 해준다. (메시지 받고, 전달까지)
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId형태로 메시지를 발생 시 MessageMapping 수신
    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    // DestinationVariable : @MessageMapping 어노테이션 정의된 Websocket Controller내에서만 사용
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        System.out.println(message);

        return message; // 리턴해주면 @SendTo에서 이 메시지를 해당 토픽에 발행
    }
}
