package com.blog.controller;

import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserContorller {

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @RequestMapping(value = "/user/test", method = RequestMethod.GET, produces = {BlogMediaType.APPLICATION_PROTOBUF})
    @ResponseBody
    public BlogStore.UserItem getUser() {
        BlogStore.UserItem.Builder builder = BlogStore.UserItem.newBuilder();
        builder.setNickname("setNickname");
        builder.setEmail("setEmail");
        builder.setUsername("setUsername");
        return builder.build();
    }
}
