package com.blog.api;

import com.blog.dao.UserDao;
import com.blog.db.User;
import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import javax.ws.rs.*;

@Path("/user")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class UserApi {

    @Autowired
    DataSource dataSource;

    @GET
    @Path("/all/{isShowDelete}")
    public BlogStore.UserList getUserOfNoDelete(@PathParam("isShowDelete") boolean isShowDelete) {
        BlogStore.UserList list = null;
        try {
            Base.open(dataSource);
            list = User.builderUserList(UserDao.getAllUser(isShowDelete)).build();
        } finally {
            Base.close();
        }
        return list;
    }

    @POST
    public BlogStore.Result saveUser(BlogStore.UserItem userItem) {
        return UserDao.saveUser(userItem);
    }

}
