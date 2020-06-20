package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.config.jersey.BlogMediaType;
import com.blog.mybatis.entity.User;
import com.blog.mybatis.service.UserService;
import com.blog.proto.BlogStore;
import com.blog.sso.BlogUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author haijun.zhang
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping(value = "/token", produces = {BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.UserItem tokenUser() {
        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        if (aut != null && aut.getPrincipal() instanceof BlogUserDetails) {
            return ((BlogUserDetails) aut.getPrincipal()).getUser().bulidUserItem();
        } else if (aut != null && aut.getPrincipal() instanceof String) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, aut.getPrincipal());
            User dbUser = service.getOne(queryWrapper);
            if (dbUser != null) {
                return dbUser.bulidUserItem();
            }
        }
        return BlogStore.UserItem.getDefaultInstance();
    }

    @GetMapping(value = "/all", produces = {BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.UserList usersList() {
        return BlogStore.UserList.newBuilder()
                .addAllItems(service.list().stream().map(User::bulidUserItem).collect(Collectors.toList()))
                .build();
    }
}
