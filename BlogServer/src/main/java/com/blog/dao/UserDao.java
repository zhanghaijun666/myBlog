package com.blog.dao;

import com.blog.db.User;
import com.blog.proto.BlogStore;

import java.util.List;

public class UserDao {
    public static List<User> getValidUser() {
        return User.find("status = ? ", BlogStore.Status.StatusActive_VALUE);
    }

    public static List<User> getAllUser(boolean isShowDelete) {
        if (isShowDelete) {
            return User.findAll();
        } else {
            return User.find("status != ? ", BlogStore.Status.StatusDeleted_VALUE);
        }
    }
}
