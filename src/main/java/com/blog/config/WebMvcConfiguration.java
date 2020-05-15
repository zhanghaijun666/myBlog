package com.blog.config;

import com.blog.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


@Configuration
@EnableWebMvc
@Import(BlogSetting.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final BlogSetting blogSetting;

    public WebMvcConfiguration(BlogSetting blogSetting) {
        this.blogSetting = blogSetting;
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "static"), "application/x-javascript;charset=utf-8")).addPathPatterns("/static/**/packed-**.js");
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "static"), "text/html;charset=utf-8")).addPathPatterns("/static/**/packed-**.html");
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "resource"), "application/x-javascript;charset=utf-8")).addPathPatterns("/resource/**/packed-**.js");
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "resource"), "text/html;charset=utf-8")).addPathPatterns("/resource/**/packed-**.html");
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "resource"), "text/css;charset=UTF-8")).addPathPatterns("/resource/**/packed-**.css");
        registry.addInterceptor(new MvcMultipleInterceptor(PathUtils.joinPath(blogSetting.getUIDir(), "resource"), "text/css;charset=UTF-8")).addPathPatterns("/resource/**/packed-**.css");
    }

    //页面跳转
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/index", "/");
    }

    //静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + PathUtils.joinPath(PathUtils.getBlogServerPath(), blogSetting.getUIDir(), "static/"));
        registry.addResourceHandler("/resource/**").addResourceLocations("file:" + PathUtils.joinPath(PathUtils.getBlogServerPath(), blogSetting.getUIDir(), "resource/"));
//        registry.addResourceHandler("/template/**").addResourceLocations("classpath:/templates/");
    }

    //默认静态资源处理器
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    //视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    }

    //跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET").maxAge(3600 * 24);
    }
}
