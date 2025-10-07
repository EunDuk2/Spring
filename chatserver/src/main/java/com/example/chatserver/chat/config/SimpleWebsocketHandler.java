//package com.example.chatserver.chat.config;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//// connect로 웹소켓 연결요청이 들어왔을 때, 이를 처리할 클래스
//@Component
//public class SimpleWebsocketHandler extends TextWebSocketHandler {
//
//    // 연결된 세션 관리: thread safe한 set사용
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//    // 연결되면 처리
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//        System.out.println("connected to " + session.getId());
//    }
//
//    // message가 들어왔을 때, 어떡할건지
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("receive message from " + session.getId() + ": " + payload);
//
//        // 수신가능한 모든 플레이어들에게 메시지를 전송
//        for(WebSocketSession s : sessions){
//            if(s.isOpen()){
//                s.sendMessage(new TextMessage(payload));
//            }
//        }
//    }
//
//    // 연결이 끊기면 처리
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        System.out.println("disconnected from " + session.getId());
//    }
//}
