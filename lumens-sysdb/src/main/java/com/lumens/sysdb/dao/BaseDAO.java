package com.lumens.sysdb.dao;

import com.lumens.sysdb.SQLManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class BaseDAO {

    protected SQLManager sqlManager;
    protected JdbcTemplate jdbcTemplate;
    protected TransactionTemplate transactionTemplate;

    protected class TotalRowCallbackHandler implements RowCallbackHandler {

        private int total;

        public int getTotal() {
            return total;
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            total = rs.getInt(1);
        }
    }

    public int getTotal(String sqlUri) {
        TotalRowCallbackHandler total = new TotalRowCallbackHandler();
        jdbcTemplate.query(sqlManager.getSQL(sqlUri), total);
        return total.getTotal();
    }

    public void setSqlManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
