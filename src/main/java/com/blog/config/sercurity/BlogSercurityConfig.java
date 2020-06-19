package com.blog.config.sercurity;

import com.blog.filter.JWTAuthenticationFilter;
import com.blog.filter.JWTLoginFilter;
import com.blog.config.DataSourceConfig;
import com.blog.mybatis.service.UserService;
import com.blog.sso.BlogUserDetailService;
import com.blog.utils.ResponseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                .antMatchers("/h2/**","/druid/**").permitAll() // 放行 H2 的请求
                .and().csrf().ignoringAntMatchers("/h2/**","/druid/**") // 禁用 H2 控制台的 CSRF 防护
                .and().headers().frameOptions().sameOrigin();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/label").permitAll()
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

    /**
     * 用来解决认证过的用户访问无权限资源时的异常
     */
    private class CustomAccessDeineHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    /**
     * 用来解决匿名用户访问无权限资源时的异常
     */
    private class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    /**
     * 登录成功以后的处理
     */
    private class AuthenticationSuccess implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setContentType("application/json;charset=UTF-8");
            ResponseUtils.write(response, "");
        }
    }

    /**
     * 登录异常的处理
     */
    private class AuthenticationFailure implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("application/json;charset=UTF-8");
            ResponseUtils.write(response, "{error:'" + exception.getMessage() + "'}");
        }
    }

    /**
     * 多终端登录，session失效处理。
     */
    private class SessionInformationExpiredStrategyImpl implements SessionInformationExpiredStrategy {
        @Override
        public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
            event.getResponse().setStatus(HttpStatus.CONFLICT.value());
            ResponseUtils.write(event.getResponse(), "你的账号在另一地点被登录");
        }
    }
}
