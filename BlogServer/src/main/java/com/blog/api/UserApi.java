package com.blog.api;

import com.blog.dao.UserDao;
import com.blog.db.User;
import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Base;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import javax.ws.rs.*;

@Path("/user")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Slf4j
public class UserApi {

    @Autowired
    DataSource dataSource;

    @GET
    @Path("/all/{isShowDelete}")
    public BlogStore.UserList getUserOfNoDelete(@PathParam("isShowDelete") boolean isShowDelete) {
        BlogStore.UserList list = null;
        try {
//            Base.open(dataSource);
            Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/blogdb", "root", "123456");
//            Base.openTransaction();
            return User.builderUserList(UserDao.getAllUser(isShowDelete)).build();
//            Base.commitTransaction();
        } catch (Exception e) {
            log.warn("warn message", e);
//            log.info("=====>>>>> logger()");
//            Base.rollbackTransaction();
        } finally {
            Base.close();
        }
        return BlogStore.UserList.newBuilder().addItems(BlogStore.UserItem.newBuilder().setNickname("setNickname").setUsername("setUsername")).build();
    }

    @POST
    public BlogStore.Result saveUser(BlogStore.UserItem userItem) {
        return UserDao.saveUser(userItem);
    }

}
