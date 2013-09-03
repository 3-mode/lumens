package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.PersonSummaryListRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class PersonSummaryListDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<PersonSummaryListRecord> getPersonSummaryRecordList() throws Exception {
        final List<PersonSummaryListRecord> pList = new ArrayList<PersonSummaryListRecord>();
        jdbcTemplate.query(PersonSummaryListRecord.SQL_ALL, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(PersonSummaryListRecord.class, rs));
            }
        });
        return pList;
    }
}
