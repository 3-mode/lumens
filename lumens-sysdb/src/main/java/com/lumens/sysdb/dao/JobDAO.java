/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.EntityFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobDAO extends BaseDAO {
    public int getTotal() {
        return super.getTotal("JobDAO/total");
    }
    
    public long create(final Job job) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("JobDAO/CreateJob"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setLong(1, job.id);
                            ps.setString(2, job.name);
                            ps.setString(3, job.description);
                            ps.setInt(4, job.repeatCount);
                            ps.setInt(5, job.interval);
                            ps.setTimestamp(6, job.startTime);
                            ps.setTimestamp(7, job.endTime);
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
        return job.id;
    }

    public long update(final Job job) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("JobDAO/UpdateJob"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {                            
                            ps.setString(1, job.name);
                            ps.setString(2, job.description);
                            ps.setInt(3, job.repeatCount);
                            ps.setInt(4, job.interval);
                            ps.setTimestamp(5, job.startTime);
                            ps.setTimestamp(6, job.endTime);
                            ps.setLong(7, job.id);
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
        return job.id;
    }

    public long delete(final long jobId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("JobDAO/DeleteJob"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setLong(1, jobId);
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
        return jobId;
    }  
    
    public Job getJob(long jobId) {
        final List<Job> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("JobDAO/FindJob", jobId), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Job.class, rs));
            }
        });
        return pList.size() > 0 ? pList.get(0) : null;
    }    
    
    public List<Job> getAllJob() {
        final List<Job> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("JobDAO/AllJob"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Job.class, rs));
            }
        });
        return pList;
    }    
}
