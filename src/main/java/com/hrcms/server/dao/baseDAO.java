package com.hrcms.server.dao;

import com.hrcms.server.dao.sql.SQLManager;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class baseDAO {
    protected SQLManager sqlManager;
    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setSqlManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
