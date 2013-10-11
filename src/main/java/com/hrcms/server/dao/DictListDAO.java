package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.DictListRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class DictListDAO extends baseDAO {
    public List<DictListRecord> getDictTableList() throws Exception {
        final List<DictListRecord> pList = new ArrayList<DictListRecord>();
        jdbcTemplate.query(sqlManager.getSQL("DictListDAO/tableNames"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(DictListRecord.class, rs));
            }
        });
        return pList;
    }
}
