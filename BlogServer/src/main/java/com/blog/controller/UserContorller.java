package com.blog.controller;

import com.blog.proto.BlogStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserContorller {

    @RequestMapping(value = "/user/test", method = RequestMethod.GET, produces = "application/x-protobuf")
    @ResponseBody
    public BlogStore.UserItem getUser() {
        BlogStore.UserItem.Builder builder = BlogStore.UserItem.newBuilder();
        builder.setFullName("setFullName");
        builder.setEmail("setEmail");
        builder.setUsername("setUsername");
        return builder.build();
    }
}
