package com.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "blog")
@Data
//@PropertySource(value = "classpath:blog.properties")//配置文件路径  在resource目录下
//@PropertySource(value = "file:blog.proferties")//配置文件路径  在当前目录下
public class BlogSetting {

    private String UIDir;
}

