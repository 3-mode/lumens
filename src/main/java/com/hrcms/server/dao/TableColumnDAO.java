package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.TableColumn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class TableColumnDAO extends baseDAO {
    public List<TableColumn> getColumnList(String dictName) throws Exception {
        final List<TableColumn> pList = new ArrayList<TableColumn>();
        jdbcTemplate.query(String.format(TableColumn.TABLECOLUMNS, dictName), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(TableColumn.class, rs));
            }
        });
        return pList;
    }
}
