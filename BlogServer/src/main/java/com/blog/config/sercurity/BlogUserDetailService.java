package com.blog.config.sercurity;

import com.blog.config.DataSourceConfig;
import com.blog.db.User;
import com.blog.proto.BlogStore;
import com.blog.service.login.LoginAuthority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    class BlogUserDetails implements UserDetails {
        private final User user;

        public BlogUserDetails(User dbUser) {
            this.user = dbUser;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authList = new ArrayList<>();
            authList.add(new SimpleGrantedAuthority("USER"));
            authList.add(new SimpleGrantedAuthority("ADMIN"));
            authList.add(new SimpleGrantedAuthority("DBA"));
            return authList;
        }

        @Override
        public String getPassword() {
            return new BCryptPasswordEncoder().encode(this.user.getPassword());
        }

        @Override
        public String getUsername() {
            return this.user.getName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return this.user.getStatus() == BlogStore.Status.StatusActive;
        }
    }
}
