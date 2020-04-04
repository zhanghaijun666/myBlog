package com.blog.config;

import com.blog.service.login.BlogUserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@Configuration
public class BlogSercurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationSuccess authenticationSuccess;
    private final AuthenticationFailure authenticationFailure;
    private final BlogUserDetailService detailsService;

    public BlogSercurityConfig(AuthenticationSuccess success, AuthenticationFailure failure) {
        this.authenticationSuccess = success;
        this.authenticationFailure = failure;
        this.detailsService = new BlogUserDetailService();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/static/**", "/template/**", "/login", "/logout").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and().formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/template/login.html")
                .successHandler(authenticationSuccess)//登陆成功处理
                .failureHandler(authenticationFailure)//登录失败的处理
                .loginProcessingUrl("/login")//登录请求地址
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/template/login.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);//.passwordEncoder(passwordEncoder());
    }

//    @Beanu
//    public SecurityContextRepository securityContextRepository() {
    //设置对spring security的UserDetails进行session保存,这个必须要有，不然不会保存至session对应的缓存redis中
//        return new HttpSessionSecurityContextRepository();
//    }
}

@Component("authenticationSuccess")
class AuthenticationSuccess implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getSession().setAttribute("USERSESSION", authentication.getName());
        new DefaultRedirectStrategy().sendRedirect(request, response, "/test");
    }
}

@Component("authenticationFailure")
class AuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String meg = "登录失败";
        request.getRequestDispatcher("/template/404.html?meg=" + meg).forward(request, response);
    }
}
