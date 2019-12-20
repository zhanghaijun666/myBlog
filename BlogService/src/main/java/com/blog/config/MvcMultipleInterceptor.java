package com.blog.config;

import com.blog.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

//ResourceHttpRequestHandler
public class MvcMultipleInterceptor implements HandlerInterceptor {

    private static final byte[] NEWLINE = "\r\n".getBytes();
    private String UIDir;
    private String mediaType;

    public MvcMultipleInterceptor(String UIDir, String mediaType) {
        this.UIDir = UIDir;
        this.mediaType = mediaType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!StringUtils.equals(request.getMethod(), "GET")) {
            return true;
        }
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (path == null) {
            return true;
        }
        File file = new File(PathUtils.joinPath(System.getProperty("user.dir"), this.UIDir, path));
        if (!file.exists() && file.getName().startsWith("packed-")) {
            file = new File(PathUtils.joinPath(PathUtils.getBlogServerPath(), this.UIDir, path + ".txt"));
        }
        if (!file.exists() || !file.canRead()) {
            return true;
        }
        response.setContentType(mediaType);
        writeResource(file, response.getOutputStream());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle.......");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("afterCompletion.......");
    }

    private void writeResource(File file, final OutputStream out) throws IOException {
        if (file.getName().startsWith("packed-")) {
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String filePath;
                while ((filePath = in.readLine()) != null) {
                    filePath = filePath.trim();
                    if (filePath.length() > 0 && !filePath.startsWith("#")) {
                        File innerFile = new File(file.getParent(), filePath);
                        writeResource(innerFile, out);
                        out.write(NEWLINE);
                    }
                }
            }
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                writeResource(childFile, out);
                out.write(NEWLINE);
            }
        } else {
            byte[] buffer = new byte[2048];
            try (BufferedInputStream innerStream = new BufferedInputStream(new FileInputStream(file))) {
                int bytesSize;
                while ((bytesSize = innerStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesSize);
                }
                out.flush();
            }
        }
    }
}
