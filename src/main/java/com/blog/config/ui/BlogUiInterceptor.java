package com.blog.config.ui;

import com.blog.utils.PathUtils;
import com.blog.utils.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;

public class BlogUiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!StringUtils.equals(request.getMethod(), "GET")) {
            return true;
        }
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String parentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "ui";
        if (StringUtils.isBlank(parentPath) || StringUtils.isBlank(path) || !PathUtils.getFileName(path).startsWith("packed-")) {
            return true;
        }
        File file = new File(parentPath, path);
        if (!file.exists() && !file.getName().endsWith(".txt")) {
            file = new File(parentPath, path + ".txt");
        }
        if (!file.exists() || !file.canRead()) {
            return true;
        }
        try (OutputStream outputStream = response.getOutputStream()) {
            if (file.getName().endsWith(".js")) {
                response.setContentType("application/x-javascript;charset=utf-8");
            } else if (file.getName().endsWith(".html")) {
                response.setContentType("text/html;charset=utf-8");
            } else if (file.getName().endsWith(".css")) {
                response.setContentType("text/css;charset=UTF-8");
            }
            ResourceUtils.writeResource(file, outputStream);
        }
        return false;
    }
}
