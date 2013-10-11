package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.ResumeItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class ResumeItemDAO extends baseDAO {
    public List<ResumeItem> getResumeItemList() throws Exception {

        final List<ResumeItem> pList = new ArrayList<ResumeItem>();
        jdbcTemplate.query(ResumeItem.SQL_ALL_RESUMEITEMS, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(ResumeItem.class, rs));
            }
        });
        return pList;
    }
}
