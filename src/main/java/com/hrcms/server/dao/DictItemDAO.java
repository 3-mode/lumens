package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.DictItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DictItemDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<DictItem> getDictItemList(String dictName) throws Exception {
        final List<DictItem> pList = new ArrayList<DictItem>();
        jdbcTemplate.query(String.format(DictItem.SQL_ALL, dictName), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(DictItem.class, rs));
            }
        });
        return pList;
    }
}
