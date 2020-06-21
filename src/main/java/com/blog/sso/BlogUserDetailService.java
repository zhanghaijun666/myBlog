package com.blog.sso;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.mybatis.entity.User;
import com.blog.mybatis.service.UserService;
import com.blog.proto.BlogStore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class BlogUserDetailService implements UserDetailsService {
    private final UserService userService;

    public BlogUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("UserName is Blank.");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User dbUser = userService.getOne(queryWrapper);
        if (null == dbUser) {
            dbUser = checkUserInfo(username);
        }
        if (null == dbUser) {
            throw new UsernameNotFoundException("User not find.");
        }
        return new BlogUserDetails(dbUser);
    }

    public User checkUserInfo(String user) {
        File file = new File("user.txt");
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.startsWith("#")) {
                    continue;
                }
                String[] fields = tempString.split(":");
                if (fields == null || fields.length < 2) {
                    continue;
                }
                String platUser = fields[0].trim();
                String platPass = fields[1].trim();
                if (StringUtils.isBlank(platUser) || StringUtils.isBlank(platPass)) {
                    continue;
                }
                if (StringUtils.equals(user, platUser)) {
                    User dbUser = new User();
                    dbUser.setUsername(user);
                    dbUser.setPassword(platPass);
                    dbUser.setStatus(BlogStore.Status.StatusActive_VALUE);
                    if (userService.save(dbUser)) {
                        return dbUser;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
