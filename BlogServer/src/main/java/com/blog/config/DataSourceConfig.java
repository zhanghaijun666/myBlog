package com.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootConfiguration
@Data
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriver;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.name}")
    private String jdbcUser;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Bean
    @Primary //选择默认的数据源
    public DataSource createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
//        dataSource.setMaxActive(200);
//        dataSource.setMaxWait(100);
        dataSource.setValidationQuery("SELECT 1 FROM dual");
        return dataSource;
    }
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow","127.0.0.1");
        reg.addInitParameter("loginUsername", "admin");
        reg.addInitParameter("loginPassword", "admin");
        reg.addInitParameter("logSlowSql", "true");
        //是否可以重置数据
        reg.addInitParameter("resetEnable","false");
        return reg;
    }
    /**
     * 过滤规则
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "/static/*,/resource/*,/h2/*,/druid/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }
}
