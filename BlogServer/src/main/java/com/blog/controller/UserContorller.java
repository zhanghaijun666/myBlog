package com.blog.controller;

import com.blog.config.sercurity.BlogUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class UserContorller {

    @RequestMapping(value = "/login-user", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyRole('USER')")
    public String currentUserName(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Principal principal = request.getUserPrincipal();
        if (null != principal && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            json.put("name", authentication.getName());
            json.put("authority", StringUtils.join(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()), ","));
            if (authentication.getPrincipal() instanceof BlogUserDetails) {
                BlogUserDetails userDetail = (BlogUserDetails) authentication.getPrincipal();
                json.put("userId",userDetail.getUser().getUserId());
            }
        }
        return json.toString();
    }
}
