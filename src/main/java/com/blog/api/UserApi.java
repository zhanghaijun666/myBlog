package com.blog.api;

import com.blog.config.jersey.BlogMediaType;
import com.blog.mybatis.entity.User;
import com.blog.mybatis.service.UserService;
import com.blog.proto.BlogStore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
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

    @POST
    @Path("/save")
    public BlogStore.Result saveUsere(BlogStore.UserItem item) {
        if (service.save(User.toBulid(item))) {
            return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK).build();
        }
        return BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_ERROR).build();
    }
}
