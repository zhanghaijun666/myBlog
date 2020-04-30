package com.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@EnableScheduling
@Controller
@Slf4j
public class WebSocketContorller {

    @Autowired
    private SimpMessagingTemplate template;

    //系统公告
    @MessageMapping("/notice")
    @SendTo("/topic/notice")
    public String systemMotice(String message) {
        return "message:" + message;
    }

    @MessageMapping(value = "/alone")
    public void handleChat(String messageStr) {
        this.template.convertAndSendToUser("a", "/alone/getResponse", messageStr);
    }
}
