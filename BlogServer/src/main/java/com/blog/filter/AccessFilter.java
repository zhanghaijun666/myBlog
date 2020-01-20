package com.blog.filter;

import com.blog.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "accessFilter", urlPatterns = {"/*"})
@Order(1) //值越小，Filter越靠前。
@Slf4j
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        filterChain.doFilter(request, response);
        log.info(
                "IPAddress: " + RequestUtils.getIPAddress(request)
                + ";  AccessUrl: " + RequestUtils.getAccessUrl(request)
        );
    }


}
