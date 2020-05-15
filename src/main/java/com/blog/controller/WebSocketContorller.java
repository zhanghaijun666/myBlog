package com.blog.controller;

import com.blog.config.annotation.ChatRecord;
import com.blog.config.webSocket.StompConstant;
import com.blog.db.User;
import com.blog.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@EnableScheduling
@Controller
@Slf4j
public class WebSocketContorller {

    @Autowired
    private SimpMessagingTemplate template;

    //系统公告
    @MessageMapping("/notice")
    @SendTo("/topic/notice")
    @ChatRecord
    public String systemMotice(Principal principal, String message) {
        return "message:" + message;
    }

    @MessageMapping(StompConstant.PUB_CHAT_ROOM)
    @ChatRecord
    public void chatRoom(String messageStr, Principal principal) {
        this.template.convertAndSend(StompConstant.SUB_CHAT_ROOM, "全部人都会收到：" + messageStr);
    }

    /**
     * 发送消息到指定用户
     */
    @ChatRecord
    @MessageMapping(StompConstant.PUB_USER)
    public void sendToUser(String messageStr, Principal principal) throws Exception {
        User dbUser = RequestUtils.getUser(principal);
        this.template.convertAndSendToUser(dbUser.getName(), StompConstant.SUB_USER, dbUser.getName() + "会收到：" + messageStr);
    }
}
