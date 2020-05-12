package com.blog.server;

import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@EnableHasor()
@EnableHasorWeb()
@SpringBootApplication
@ComponentScan({"com.blog.controller", "com.blog.config"})
@ServletComponentScan(basePackages = "com.blog.filter")
public class BlogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServiceApplication.class, args);
    }

}
