package com.blog.serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
@ComponentScan({"com.blog.config", "com.blog.filter", "com.blog.controller", "com.blog.mybatis"})
@MapperScan("com.blog.mybatis.mapper.xml")
public class BlogServeApplication {

    public static void main(String[] args) {
//        SpringApplication.run(BlogServeApplication.class, args);
        SpringApplication app = new SpringApplication(BlogServeApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
    }
    /**
     * protobuf 序列化
     */
    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    /**
     * protobuf 反序列化
     */
    @Bean
    RestTemplate restTemplate(ProtobufHttpMessageConverter protobufHttpMessageConverter) {
        return new RestTemplate(Collections.singletonList(protobufHttpMessageConverter));
    }

}
