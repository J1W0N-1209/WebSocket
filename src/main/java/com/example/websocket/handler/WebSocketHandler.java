package com.example.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

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
