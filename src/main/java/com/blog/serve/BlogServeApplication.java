package com.blog.serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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

}
