package com.blog.api;

import com.blog.dao.UserDao;
import com.blog.db.User;
import com.blog.jersey.BlogMediaType;
import com.blog.proto.BlogStore;
import com.blog.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/user")
//@RolesAllowed("user")
@Produces({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
@Consumes({BlogMediaType.APPLICATION_JSON, BlogMediaType.APPLICATION_PROTOBUF})
public class UserApi {

    @GET
    public BlogStore.UserItem getLoginUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user = RequestUtils.getUser(request);
        if (null == user) {
            throw new WebApplicationException(javax.ws.rs.core.Response.ok().status(Response.Status.UNAUTHORIZED.getStatusCode()).type(BlogMediaType.TEXT_HTML).build());
        }
        return user.builderUserItem().build();
    }

    @GET
    @Path("/all/{isShowDelete}")
    public BlogStore.UserList getUserOfNoDelete(@PathParam("isShowDelete") boolean isShowDelete) {
        return User.builderUserList(UserDao.getAllUser(isShowDelete)).build();
    }

    @POST
    public BlogStore.Result saveUser(BlogStore.UserItem userItem) {
        return UserDao.saveUser(userItem);
    }

    @DELETE
    public BlogStore.Result deleteUser(BlogStore.UserList list) {
        Set<Integer> userIds = list.getItemsList().stream().map(BlogStore.UserItem::getUserId).collect(Collectors.toSet());
        int num = 0;
        if (null != userIds && !userIds.isEmpty()) {
            num = User.update(User.STATUS + " =? ", "id in (" + StringUtils.join(userIds, ",") + ")", BlogStore.Status.StatusDeleted_VALUE);
        }
        return BlogStore.Result.newBuilder().setCode(num > 0 ? BlogStore.ReturnCode.RETURN_OK : BlogStore.ReturnCode.RETURN_ERROR).build();
    }
}
