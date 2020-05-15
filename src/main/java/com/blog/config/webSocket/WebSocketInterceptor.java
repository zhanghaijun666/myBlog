package com.blog.config.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;

/**
 * 输入消息拦截器
 */
@Component
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

    /**
     * 绑定用户信息
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal username = accessor.getUser();
            if (StringUtils.isEmpty(username)) {
                return null;
            }
            accessor.setUser(username);
        }
        return message;
    }


    /**
     * 在发送完成后调用，无论是否已经引发任何异常，从而允许适当的资源清理。
     * 请注意，只有preSend成功完成并返回一条消息，也就是说它没有返回null,才会调用该方法
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info(this.getClass().getCanonicalName() + " 订阅消息发送成功");
        }
        //如果用户断开连接
        if (StompCommand.DISCONNECT.equals(command)) {
            log.info(this.getClass().getCanonicalName() + "用户断开连接成功");
        }
    }

}
