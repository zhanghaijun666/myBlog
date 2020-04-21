package com.blog.controller;

import com.blog.config.BlogSetting;
import com.blog.utils.PathUtils;
import com.blog.utils.ResourceUtils;
import com.blog.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

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
        File file = new File(PathUtils.joinPath(PathUtils.getBlogServerPath(), blogSetting.getUIDir(), "/static/packed-index.html.txt"));
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("text/html;charset=utf-8");
            ResourceUtils.writeResource(file, outputStream);
        }
    }

    @RequestMapping("/getVerifyCode")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = VerifyCodeUtil.getVerifyCode();
        HttpSession session = request.getSession();
        session.setAttribute(VerifyCodeUtil.SESSION_KEY, map.get(VerifyCodeUtil.SESSION_KEY));
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        try {
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write((RenderedImage) map.get(VerifyCodeUtil.BUFFIMG_KEY), "jpeg", sos);
            sos.close();
            //设置验证码过期时间
            VerifyCodeUtil.removeAttrbute(session);
        } catch (IOException e) {
            e.printStackTrace();
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
