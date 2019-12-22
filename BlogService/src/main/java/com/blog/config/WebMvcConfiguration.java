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

    @Autowired
    private BlogSetting blogSetting;

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MvcMultipleInterceptor(blogSetting.getUIDir(),"application/x-javascript;charset=utf-8")).addPathPatterns("/static/**/packed-**.js");
        registry.addInterceptor(new MvcMultipleInterceptor(blogSetting.getUIDir(),"text/html;charset=utf-8")).addPathPatterns("/static/**/packed-**.html");
    }

    //页面跳转
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/index", "/static/main/packed-index.html");
    }

    //静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:"+ PathUtils.joinPath(PathUtils.getBlogServerPath(),blogSetting.getUIDir()));
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

    //信息转换器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
/*
        /* 是否通过请求Url的扩展名来决定media type * /
        configurer.favorPathExtension(true)
                /* 不检查Accept请求头 * /
                .ignoreAcceptHeader(true)
                .parameterName("mediaType")
                /* 设置默认的media yype * /
                .defaultContentType(MediaType.TEXT_HTML)
                /* 请求以.html结尾的会被当成MediaType.TEXT_HTML* /
                .mediaType("html", MediaType.TEXT_HTML)
                /* 请求以.json结尾的会被当成MediaType.APPLICATION_JSON* /
                .mediaType("json", MediaType.APPLICATION_JSON);
*/
    }
}
