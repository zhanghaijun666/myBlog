package com.blog.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 聊天记录切面类
 */
@Aspect
@Component
@Slf4j
public class ChatRecordAspect {
    @Pointcut("@annotation(com.blog.config.annotation.ChatRecord)")
    public void chatRecordPointcut() {
    }
    @Before("chatRecordPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        //获取聊天记录，存到文件中
    }
}