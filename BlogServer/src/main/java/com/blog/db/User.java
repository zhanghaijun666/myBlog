package com.blog.db;

import com.blog.proto.BlogStore;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.Collection;

@Table("user")
public class User extends Model implements CommonModel {
    public static final int DEFAULT_USER_ID = 0;

    public int getUserId() {
        return getInteger("id");
    }

    public String getName() {
        return getString("username");
    }

    public String getNickname() {
        String nick = getString("nickname");
        if (null == nick || StringUtils.isBlank(nick)) {
            return getName();
        }
        return nick;
    }

    public String getPassword() {
        return getString("password");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getPhone() {
        return getString("phone");
    }

    public Long getBirthday() {
        return getLong("birthday");
    }

    public BlogStore.UserItem.Builder builderUserItem() {
        return BlogStore.UserItem.newBuilder()
                .setUsername(this.getName())
                .setNickname(this.getNickname())
                .setEmail(this.getEmail())
                .setPhone(this.getPhone())
                .setBirthday(this.getBirthday())
                .setStatus(this.getStatus());
    }

    public static BlogStore.UserList.Builder builderUserList(Collection<User> list) {
        BlogStore.UserList.Builder builder = BlogStore.UserList.newBuilder();
        if (list != null && !list.isEmpty()) {
            for (User user : list) {
                builder.addItems(user.builderUserItem());
            }
        }
        return builder;
    }
}
