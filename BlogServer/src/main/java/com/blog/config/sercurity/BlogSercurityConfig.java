package com.blog.config.sercurity;

import com.blog.config.DataSourceConfig;
import com.blog.utils.ResponseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@EnableWebSecurity
@Configuration
@Import(DataSourceConfig.class)
public class BlogSercurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    BlogSercurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //允许同源使用 iframe 处理h2控制台无法访问的情况
        http.headers().frameOptions().sameOrigin();
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/static/**", "/template/**", "/login").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .antMatchers("/h2/**", "/druid/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and().formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/template/login.html")
                .successHandler(new AuthenticationSuccess())//登陆成功处理
                .failureHandler(new AuthenticationFailure())//登录失败的处理
                .loginProcessingUrl("/login")//登录请求地址
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/template/login.html")
                .permitAll()
                .and().exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeineHandler())
                .and().sessionManagement()
                .invalidSessionUrl("/template/login.html")
                .maximumSessions(1)
                .expiredSessionStrategy(new SessionInformationExpiredStrategyImpl());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new BlogUserDetailService(dataSource)).passwordEncoder(new BCryptPasswordEncoder());
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
            new DefaultRedirectStrategy().sendRedirect(request, response, "/");
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

