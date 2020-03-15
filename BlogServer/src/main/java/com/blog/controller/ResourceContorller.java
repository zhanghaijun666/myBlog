package com.blog.controller;

import com.blog.config.BlogSetting;
import com.blog.utils.PathUtils;
import com.blog.utils.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/")
public class ResourceContorller {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final BlogSetting blogSetting;

    @Autowired
    public ResourceContorller(HttpServletRequest request, HttpServletResponse response, BlogSetting blogSetting) {
        this.request = request;
        this.response = response;
        this.blogSetting = blogSetting;
    }

    @RequestMapping("/")
    public void rootResourceHandle() throws IOException {
        File file = new File(PathUtils.joinPath(PathUtils.getBlogServerPath(), blogSetting.getUIDir(), "/main/packed-index.html.txt"));
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("text/html;charset=utf-8");
            ResourceUtils.writeResource(file, outputStream);
        }

    }

    @RequestMapping("/test")
    @ResponseBody
    public String resourceHandle() {
        return "test";
    }
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


}
