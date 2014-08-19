/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server.sql.dao;

import com.lumens.server.sql.EntityFactory;
import com.lumens.server.sql.entity.Project;
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

public class ProjectDAO extends BaseDAO {

    public int getTotal() {
        return super.getTotal("ProjectDAO/total");
    }

    public String create(final Project project) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("ProjectDAO/CreateProject"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setString(1, project.id);
                            ps.setString(2, project.name);
                            ps.setString(3, project.description);
                            ps.setString(4, project.data);
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
        return project.id;
    }

    public String update(final Project project) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
                try {
                    jdbcTemplate.execute(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                            return cnctn.prepareStatement(sqlManager.getSQL("ProjectDAO/UpdateProject"));
                        }
                    }, new PreparedStatementCallback<Boolean>() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                            ps.setString(1, project.name);
                            ps.setString(2, project.description);
                            ps.setString(3, project.data);
                            ps.setString(4, project.id);
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
        return project.id;
    }

    public List<Project> getAllShortProject() {
        final List<Project> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("ProjectDAO/AllShortProject"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Project.class, rs));
            }
        });
        return pList;
    }

    public Project getProject(String projectId) {
        final List<Project> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("ProjectDAO/FindProject", projectId), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Project.class, rs));
            }
        });
        return pList.size() > 0 ? pList.get(0) : null;
    }

    public List<Project> getAllProject() {
        final List<Project> pList = new ArrayList<>();
        jdbcTemplate.query(sqlManager.getSQL("ProjectDAO/AllProject"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Project.class, rs));
            }
        });
        return pList;
    }
}
