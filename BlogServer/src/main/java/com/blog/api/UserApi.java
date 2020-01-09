package com.blog.api;

import com.blog.dao.UserDao;
import com.blog.db.User;
import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;

import javax.ws.rs.*;

@Path("/user")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class UserApi {

    @GET
    @Path("/{str}")
    public String reverse(@PathParam("str") String input) {
        return new StringBuilder(input).reverse().toString();
    }

    @GET
    @Path("/test")
    public BlogStore.UserItem getUser() {
        BlogStore.UserItem.Builder builder = BlogStore.UserItem.newBuilder();
        builder.setNickname("setNickname");
        builder.setEmail("setEmail");
        builder.setUsername("setUsername");
        return builder.build();
    }

    @GET
    @Path("/all/{isShowDelete}")
    public BlogStore.UserList getUserOfNoDelete(@PathParam("isShowDelete") boolean isShowDelete) {
        return User.builderUserList(UserDao.getAllUser(isShowDelete)).build();
    }

}
