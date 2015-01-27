/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.Relation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RelationDAO extends BaseDAO {

    public void create(final long jobId, final long projectId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("RelationDAO/CreateRelation"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setLong(1, jobId);
                            ps.setLong(2, projectId);
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

    public List<Relation> getAllRelation(long jobId) {
        final List<Relation> pList = new ArrayList<>();
        jdbcTemplate.execute(sqlManager.getSQL("RelationDAO/AllRelation", jobId));
        return pList;
    }

    public void deleteAllRelation(long jobId) {
        this.simpleTransactionExecute(sqlManager.getSQL("RelationDAO/DeleteAllRelation", jobId));
    }

    public void delete(final long jobId, final long projectId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("RelationDAO/DeleteRelation"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setLong(1, jobId);
                            ps.setLong(2, projectId);
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
