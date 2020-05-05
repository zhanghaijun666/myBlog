package com.blog.config.annotation;

import java.lang.annotation.*;

/**
 * 聊天记录注解
 * <p>
 * 加上这个注解的特定方法，会将聊天记录信息记录到文件中。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChatRecord {
}