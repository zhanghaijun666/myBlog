package com.blog.config.hasor;

import com.blog.config.DataSourceConfig;
import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * - Dataway 官方手册：https://www.hasor.net/web/dataway/about.html
 * - Dataway 在 OSC 上的项目地址，欢迎收藏：https://www.oschina.net/p/dataway
 * - DataQL 手册地址：https://www.hasor.net/web/dataql/what_is_dataql.html
 * - Hasor 项目的首页：https://www.hasor.net/web/index.html
 */
@DimModule
@Component
@Import(DataSourceConfig.class)
public class DatawayModule implements SpringModule {
    private DataSource dataSource;

    public DatawayModule(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
    }
}
