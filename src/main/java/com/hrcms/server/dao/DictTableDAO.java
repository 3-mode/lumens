/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.DictTable;
import com.hrcms.server.model.PersonSummary;
import com.hrcms.server.model.PersonSummaryListRecord;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author washaofe
 */
public class DictTableDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<DictTable> getDictTableList() throws Exception {
        final List<DictTable> pList = new ArrayList<DictTable>();
        jdbcTemplate.query(DictTable.TABLENAMES, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(DictTable.class, rs));
            }
        });
        return pList;
    }
}
