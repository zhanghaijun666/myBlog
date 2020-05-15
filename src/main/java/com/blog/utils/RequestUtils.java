package com.blog.utils;

import com.blog.config.sercurity.BlogUserDetails;
import com.blog.db.User;
import com.blog.service.File.FileUrl;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class RequestUtils {
    public static int getUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        if (null == user) {
            return FileUrl.DEFAULT_OWNER_ID;
        }
        return user.getUserId();
    }

    public static User getUser(java.security.Principal principal) {
        if (principal instanceof org.springframework.security.core.Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication.getPrincipal() instanceof BlogUserDetails) {
                BlogUserDetails userDetail = (BlogUserDetails) authentication.getPrincipal();
                return userDetail.getUser();
            }
        }
        return null;
    }

    public static User getUser(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        Principal principal = request.getUserPrincipal();
        if (null != principal && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication.getPrincipal() instanceof BlogUserDetails) {
                BlogUserDetails userDetail = (BlogUserDetails) authentication.getPrincipal();
                return userDetail.getUser();
            }
        }
        return null;
    }


    public static String getAccessUrl(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return (((HttpServletRequest) request).getRequestURI());
        }
        return "unknown";
    }


    public static String getIPAddress(ServletRequest servletRequest) {
        if (!(servletRequest instanceof HttpServletRequest)) {
            return servletRequest.getRemoteAddr();
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String ipAddresses = request.getHeader("X-Forwarded-For");//X-Forwarded-For：Squid 服务代理
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");//Proxy-Client-IP：apache 服务代理
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");//WL-Proxy-Client-IP：weblogic 服务代理
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");//HTTP_CLIENT_IP：有些代理服务器
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP"); //X-Real-IP：nginx服务代理
        }
        if (ipAddresses != null && ipAddresses.length() > 0 && ipAddresses.contains(",")) {
            //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
            ipAddresses = ipAddresses.split(",")[0];
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }
        return ipAddresses.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ipAddresses;
    }
}
