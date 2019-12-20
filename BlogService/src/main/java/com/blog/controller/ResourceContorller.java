package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class ResourceContorller {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


/*
    @GetMapping("/test")
    @ResponseBody
    public String Test(HttpServletRequest request, HttpServletResponse response) {
        return "TEST";
    }

    @RequestMapping("/")
    @ResponseBody
    public String indexPage(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        return "index";
    }
*/

    @RequestMapping("/test")
    @ResponseBody
    public String resourceHandle(){
        return "test";
    }

}
