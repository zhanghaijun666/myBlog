package com.blog.api;

import com.blog.config.jersey.BlogMediaType;
import com.blog.mybatis.entity.User;
import com.blog.mybatis.service.UserService;
import com.blog.proto.BlogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.stream.Collectors;

@Path("/user")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class UserApi {

    @Autowired
    UserService service;

    @GET
    @Path("/all")
    @Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    @Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
    public BlogStore.UserList usersList() {
        return BlogStore.UserList.newBuilder()
                .addAllItems(service.list().stream().map(User::bulidUserItem).collect(Collectors.toList()))
                .build();
    }
}
