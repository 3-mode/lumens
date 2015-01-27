package com.lumens.sysdb.dao;

import com.lumens.sysdb.SQLManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
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

    public void transactionExecute(final String SQL, final Object... values) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(SQL);
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            for (int i = 0; i < values.length; ++i)
                                ps.setObject(i, values[i]);
                            return ps.execute();
                        }
                    });
                } catch (Exception e) {
                    //use this to rollback exception in case of exception
                    paramTransactionStatus.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
