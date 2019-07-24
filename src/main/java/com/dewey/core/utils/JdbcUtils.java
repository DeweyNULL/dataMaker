package com.dewey.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * The type Jdbc utils.
 *
 */
public class JdbcUtils {

    /**
     * The constant connectionTestQuery.
     */
    private static final String connectionTestQuery = "SELECT 1 FROM DUAL";
    /**
     * The constant maximumPoolSize.
     */
    private static final Integer maximumPoolSize = 10;
    /**
     * The constant maxLifetime.
     */
    private static final Long maxLifetime = 5000L;
    /**
     * The constant minimumIdle.
     */
    private static final Integer minimumIdle = 2000000;
    /**
     * The constant connectionTimeout.
     */
    private static final Integer connectionTimeout = 30000;
    /**
     * The constant idleTimeout.
     */
    private static final Integer idleTimeout = 30000;
    /**
     * The constant autoCommit.
     */
    private static final boolean autoCommit = true;

    /**
     * Init data source data source.
     *
     * @param url      the url
     * @param user     the user
     * @param password the password
     * @return the data source
     */
    public static DataSource initDataSource(String url, String user, String password){
        return initDataSource(url, user, password, connectionTestQuery, maximumPoolSize, maxLifetime, minimumIdle, connectionTimeout, idleTimeout, autoCommit);
    }

    /**
     * Init data source data source.
     *
     * @param url                 the url
     * @param user                the user
     * @param password            the password
     * @param connectionTestQuery the connection test query
     * @param maximumPoolSize     the maximum pool size
     * @param maxLifetime         the max lifetime
     * @param minimumIdle         the minimum idle
     * @param connectionTimeout   the connection timeout
     * @param idleTimeout         the idle timeout
     * @param autoCommit          the auto commit
     * @return the data source
     */
    public static DataSource initDataSource(String url, String user, String password, String connectionTestQuery,
                                            int maximumPoolSize, long maxLifetime, int minimumIdle, long connectionTimeout, long idleTimeout, boolean autoCommit){
        String driverClassName = ToolUtils.driverClassName(url);

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setConnectionTestQuery(connectionTestQuery);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMaxLifetime(maxLifetime);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setAutoCommit(autoCommit);

        return new HikariDataSource(config);
    }

    /**
     * Jdbc template jdbc template.
     *
     * @param url      the url
     * @param user     the user
     * @param password the password
     * @return the jdbc template
     */
    public static JdbcTemplate jdbcTemplate(String url, String user, String password) {
        return new JdbcTemplate(initDataSource(url, user, password));
    }


}
