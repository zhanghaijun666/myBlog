package com.blog.dao;

import com.blog.db.User;
import com.blog.proto.BlogStore;
import com.blog.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserDao {

    public static List<User> getAllUser(boolean isShowDelete) {
        if (isShowDelete) {
            return User.findAll();
        } else {
            return User.find("status != ? ", BlogStore.Status.StatusDeleted_VALUE);
        }
    }

    public static List<User> getValidUser() {
        return User.find("status = ? ", BlogStore.Status.StatusActive_VALUE);
    }

    public static List<String> getValidUserAttr(Function<? super User, ? extends String> mapper) {
        List<User> list = UserDao.getValidUser();
        if (null == list || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static List<String> getValidUserAttr(int userId, Function<? super User, ? extends String> mapper) {
        List<User> list = UserDao.getValidUser();
        if (null == list || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list.stream().filter(user -> user.getUserId() != userId).map(mapper).collect(Collectors.toList());
    }

    public static BlogStore.Result saveUser(BlogStore.UserItem userItem) {
        BlogStore.Result.Builder result = BlogStore.Result.newBuilder().setCode(BlogStore.ReturnCode.RETURN_OK);
        if (StringUtils.isBlank(userItem.getUsername())) {
            return result.setCode(BlogStore.ReturnCode.RETURN_USER_EXIST).build();
        }
        if (UserDao.getValidUserAttr(userItem.getUserId(), User::getName).contains(userItem.getUsername())) {
            return result.setCode(BlogStore.ReturnCode.RETURN_USER_EXIST).build();
        }
        if (StringUtils.isNotBlank(userItem.getPhone()) && UserDao.getValidUserAttr(userItem.getUserId(), User::getPhone).contains(userItem.getPhone())) {
            return result.setCode(BlogStore.ReturnCode.RETURN_USER_PHONE_EXIST).build();
        }
        if (StringUtils.isNotBlank(userItem.getEmail()) && UserDao.getValidUserAttr(userItem.getUserId(), User::getEmail).contains(userItem.getEmail())) {
            return result.setCode(BlogStore.ReturnCode.RETURN_USER_EMAIL_EXIST).build();
        }
        User dbUser = User.findById(userItem.getUserId());
        if (null == dbUser) {
            dbUser = User.create("username = ? ", userItem.getUsername());
        }
        dbUser.setString("nickname", userItem.getNickname());
        dbUser.setString("email", userItem.getEmail());
        dbUser.setString("phone", userItem.getPhone());
        dbUser.setLong("birthday", userItem.getBirthday());
        if (StringUtils.isBlank(dbUser.getPassword())) {
            dbUser.setString("password", RandomUtil.getRandomString(8));
            JSONObject json = new JSONObject();
            json.put("password", dbUser.getPassword());
            result.setMsg(json.toString());
        }
        dbUser.saveIt();
        return result.build();
    }
}
