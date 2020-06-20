package com.blog.config.sercurity;

import com.blog.config.DataSourceConfig;
import com.blog.filter.JWTAuthenticationFilter;
import com.blog.filter.JWTLoginFilter;
import com.blog.mybatis.service.UserService;
import com.blog.sso.BlogUserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Import(DataSourceConfig.class)
public class BlogSercurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public BlogSercurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new BlogUserDetailService(userService)).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许同源使用 iframe 处理h2控制台无法访问的情况
        http.authorizeRequests()
                .antMatchers("/h2/**", "/druid/**").permitAll() // 放行 H2 的请求
                .and().csrf().ignoringAntMatchers("/h2/**", "/druid/**") // 禁用 H2 控制台的 CSRF 防护
                .and().headers().frameOptions().sameOrigin();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .antMatchers("/admin/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/ui", "/ui/**");
    }
}
