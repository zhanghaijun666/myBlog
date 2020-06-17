package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mybatis.entity.User;
import com.blog.mybatis.service.UserService;
import com.blog.sso.BlogUserDetails;
import com.blog.sso.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haijun.zhang
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public User loginUser() {
        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        if (aut != null && aut.getPrincipal() instanceof BlogUserDetails) {
            return ((BlogUserDetails) aut.getPrincipal()).getUser();
        } else if (aut != null && aut.getPrincipal() instanceof String) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, aut.getPrincipal());
            User dbUser = service.getOne(queryWrapper);
            if (dbUser != null) {
                return dbUser;
            }
        }
        return null;
    }

    // 路由映射到/users
    @RequestMapping(value = "/all", produces = "application/json;charset=UTF-8")
    public String usersList() {
        List<User> list = service.list();
        return JSONResult.fillResultString(0, "", list);
    }

    @RequestMapping(value = "/hello", produces = "application/json;charset=UTF-8")
    public String hello() {
        ArrayList<String> users = new ArrayList<String>() {{
            add("hello");
        }};
        return JSONResult.fillResultString(0, "", users);
    }

    @RequestMapping(value = "/world", produces = "application/json;charset=UTF-8")
    public String world() {
        ArrayList<String> users = new ArrayList<String>() {{
            add("world");
        }};
        return JSONResult.fillResultString(0, "", users);
    }
}
