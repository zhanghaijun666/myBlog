package com.blog.db;

import com.blog.proto.BlogStore;
import com.blog.utils.BasicConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    public User setPassword(String password) {
        return setString("password", new BCryptPasswordEncoder().encode(password));
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
                .setUserId(this.getUserId())
                .setUsername(this.getName())
                .setNickname(BasicConvertUtils.toString(this.getNickname(), this.getName()))
                .setEmail(BasicConvertUtils.toString(this.getEmail(), ""))
                .setPhone(BasicConvertUtils.toString(this.getPhone(), ""))
                .setBirthday(BasicConvertUtils.toLong(this.getBirthday(), 0))
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
