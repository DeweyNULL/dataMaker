package com.dewey.core.config;

import com.dewey.core.utils.JdbcUtils;
import com.dewey.core.utils.ToolUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @program dataMaker
 * @description: 数据库配置
 * @author: xielinzhi
 * @create: 2019/07/11 17:12
 */

@Configuration
@MapperScan("com.dewey.core.mapper")
public class DataSourceConfig {

    @Value("${dataMaker.jdbc.jdbcUrl}")
    private String url;
    /**
     * The User.
     */
    @Value("${dataMaker.jdbc.username}")
    private String user;

    /**
     * The Password.
     */
    @Value("${dataMaker.jdbc.password}")
    private String password;
    /**
     * The Maximum pool size.
     */
    @Value("${dataMaker.jdbc.maximumPoolSize:10}")
    private int maximumPoolSize;
    /**
     * The Minimum idle.
     */
    @Value("${dataMaker.jdbc.minimumIdle:1}")
    private long minimumIdle;
    /**
     * The Max lifetime.
     */
    @Value("${dataMaker.jdbc.maxLifetime:2000000}")
    private int maxLifetime;
    /**
     * The Connection timeout.
     */
    @Value("${dataMaker.jdbc.connectionTimeout:30000}")
    private long connectionTimeout;
    /**
     * The Idle timeout.
     */
    @Value("${dataMaker.jdbc.idleTimeout:30000}")
    private long idleTimeout;

    @Value("${dataMaker.jdbc.autoCommit:true}")
    private boolean autoCommit;

    @Bean
    public DataSource originalDataSource(){
        String connectionTestQuery = ToolUtils.connectionTestSql(url);
        return JdbcUtils.initDataSource(url, user, password,
                connectionTestQuery, maximumPoolSize, minimumIdle, maxLifetime, connectionTimeout, idleTimeout, autoCommit);

    }
}
