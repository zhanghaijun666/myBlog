package com.blog.utils;

public class PathUtils {
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
