/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.sql.dao;

import com.lumens.backend.sql.EntityFactory;
import com.lumens.backend.sql.entity.InOutLogItem;
import com.lumens.backend.sql.entity.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class InOutLogDAO extends BaseDAO {
    public void create(final InOutLogItem inoutLogItem) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("InOutLogDAO/PutLog"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setLong(1, inoutLogItem.logID);
                            ps.setLong(2, inoutLogItem.componentID);
                            ps.setString(3, inoutLogItem.componentName);
                            ps.setLong(4, inoutLogItem.projectID);
                            ps.setString(5, inoutLogItem.projectName);
                            ps.setString(6, inoutLogItem.direction);
                            ps.setString(7, inoutLogItem.targetName);
                            ps.setString(8, inoutLogItem.data);
                            ps.setTimestamp(9, inoutLogItem.lastModifTime);
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

    public List<InOutLogItem> getLogList(long projectID, long componentID) {
        final List<InOutLogItem> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("InOutLogDAO/GetLogByComponent", projectID, componentID), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(InOutLogItem.class, rs));
            }
        });
        return pList;
    }

    public void deleteAllLog(long projectID) {
        jdbcTemplate.execute(sqlManager.getSQL("InOutLogDAO/ClearLog", projectID));
    }
}
