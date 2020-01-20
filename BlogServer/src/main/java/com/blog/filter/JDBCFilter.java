package com.blog.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

import com.blog.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Base;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Import(DataSourceConfig.class)
@Order(6) //值越小，Filter越靠前。
@WebFilter(filterName = "activeJDBCFilter", urlPatterns = {"/api/*"})
@Slf4j
public class JDBCFilter implements Filter {
    private final DataSource dataSource;

    public JDBCFilter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Base.open(dataSource);
            Base.openTransaction();
            filterChain.doFilter(request, response);
            Base.commitTransaction();
        } catch (IOException | ServletException e) {
            Base.rollbackTransaction();
            log.error("warn message", e);
            throw e;
        } finally {
            Base.close();
        }
    }
}
