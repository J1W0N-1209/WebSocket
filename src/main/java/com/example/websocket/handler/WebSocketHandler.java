package com.example.websocket.handler;

import com.example.websocket.dto.ChatDTO;
import com.example.websocket.dto.ChatRoom;
import com.example.websocket.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ChatService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatDTO chatMessage = mapper.readValue(payload, ChatDTO.class);
        log.info("session {}",chatMessage.toString());

        ChatRoom room = service.findRoomById(chatMessage.getRoomId());

        room.handleAction(session,chatMessage,service);
    }

    // 웹소켓이 연결됬을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} Connect",session.getId());
        sessions.add(session);
    }

    // 웹소켓이 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} Disconnect",session.getId());
        sessions.remove(session);
    }
}
