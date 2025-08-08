package com.xiaoyan.controller;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}/{sname}")
@Slf4j
public class WebSocketServer {

        public static final Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") Long sid, @PathParam("sname") String sname) {
        log.info("新用户连接：ID={}, 名称={}，Session ID={}", sid, sname, session.getId());
        sessionMap.put(sid, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") Long sid) throws IOException {
        log.info("用户{}发来信息：{}", sid, message);
        sentToOneClient(message, sid);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid 学生ID
     */
    @OnClose
    public void onClose(@PathParam("sid") Long sid) {
        log.info("用户{}断开连接：Session ID={}", sid, sessionMap.get(sid).getId());
        sessionMap.remove(sid);
    }

    /**
     * 连接发生错误时调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，Session ID={}", session.getId(), error);
        sessionMap.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    private void sentToOneClient(String message, Long id) throws IOException {
        Session session1 = sessionMap.get(id);
        if (session1 != null && session1.isOpen()) {
            session1.getBasicRemote().sendText(message);
        }
    }

}
