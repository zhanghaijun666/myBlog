package com.blog.config.webSocket;

import com.blog.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache {

    private final static ConcurrentHashMap<Integer, User> USER_MAP = new ConcurrentHashMap<>(32);

    //添加用户
    public static void addUser(int key, User user) {
        if (USER_MAP.containsKey(key)) {
            return;
        }
        USER_MAP.put(key, user);
    }

    //获取用户
    public static User getUser(int key) {
        return USER_MAP.get(key);
    }

    //删除用户
    public static void removeUser(int key) {
        USER_MAP.remove(key);
    }

    //获取在线用户数
    public static int getOnlineCount() {
        return USER_MAP.size();
    }

    //获取所有的在线用户
    public static List<User> listUser() {
        return new ArrayList<>(USER_MAP.values());
    }
}
