package com.blog.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class PathUtils {
    public static String getBlogServerPath() {
        Resource resource = new ClassPathResource("");
        try {
            return resource.getFile().getParentFile().getParentFile().getAbsolutePath();
        } catch (IOException e) {
            return System.getProperty("user.dir");
        }

    }

    public static String joinPath(Object... paths) {
        if (null == paths || paths.length == 0) {
            return "";
        }
        StringBuilder pathStr = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            pathStr.append(paths[i]).append("/");
        }
        return pathStr.substring(0, pathStr.length() - 1).replaceAll("\\/+", "/");
    }
}
