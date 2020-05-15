package com.blog.config.sercurity;

import com.blog.config.DataSourceConfig;
import com.blog.db.User;
import com.blog.service.login.LoginAuthority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.util.Objects;

@Import(DataSourceConfig.class)
public class BlogUserDetailService implements UserDetailsService {
    private final DataSource dataSource;

    BlogUserDetailService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("UserName is Blank.");
        }
        User dbUser = new LoginAuthority(dataSource).login(username);
        if (Objects.isNull(dbUser)) {
            throw new UsernameNotFoundException("user not found.");
        }
        return new BlogUserDetails(dbUser);
    }
}
