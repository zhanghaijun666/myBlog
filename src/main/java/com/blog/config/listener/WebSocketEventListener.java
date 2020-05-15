package com.blog.config.listener;


import com.blog.config.webSocket.StompConstant;
import com.blog.config.webSocket.UserCache;
import com.blog.db.User;
import com.blog.utils.RequestUtils;
import com.google.gson.JsonObject;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import javax.annotation.Resource;

/**
 * websocket事件监听
 */
@Slf4j
@Component
public class WebSocketEventListener {
    private User dbUser;
    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 建立连接监听
     */
    @EventListener
    public void handleConnectListener(SessionConnectedEvent sessionConnectedEvent) {
        dbUser = RequestUtils.getUser(sessionConnectedEvent.getUser());
        if (null != dbUser) {
            UserCache.addUser(dbUser.getUserId(), dbUser);
        }
    }

    /**
     * 断开连接监听
     */
    @EventListener
    public void handleDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        User disUser = RequestUtils.getUser(sessionDisconnectEvent.getUser());
        if (null != disUser) {
            UserCache.removeUser(dbUser.getUserId());
            JsonObject json = new JsonObject();
            json.addProperty("items", new JsonFormat().printToString(User.builderUserList(UserCache.listUser()).build()));
            json.addProperty("online", "【" + dbUser.getNickname() + "】下线了");
            // 广播离线消息
            sendMessage(json.toString());
        }
    }

    /**
     * 订阅监听
     */
    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent sessionSubscribeEvent) throws Exception {
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(sessionSubscribeEvent.getMessage(), StompHeaderAccessor.class);
        if (null != dbUser && StompConstant.SUB_STATUS.equals(stompHeaderAccessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_DESTINATION_HEADER))) {
            // 广播上线消息
            Thread.sleep(100L);
            JsonObject json = new JsonObject();
            json.addProperty("items", new JsonFormat().printToString(User.builderUserList(UserCache.listUser()).build()));
            json.addProperty("online", "【" + dbUser.getNickname() + "】上线了");
            sendMessage(json.toString());
            sendRobotMessage("机器人发消息，环境来撩我");
        }
    }

    //发送订阅消息，广播用户动态
    private void sendMessage(String messageVO) {
        messagingTemplate.convertAndSend(StompConstant.SUB_STATUS, messageVO);
    }

    //发送机器人消息
    private void sendRobotMessage(String message) {
        messagingTemplate.convertAndSend(StompConstant.SUB_CHAT_ROOM, message);
    }
}