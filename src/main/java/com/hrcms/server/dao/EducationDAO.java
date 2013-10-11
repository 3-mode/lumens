package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.EducationItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class EducationDAO extends baseDAO {
    public List<EducationItem> getEducationItemList() throws Exception {

        final List<EducationItem> pList = new ArrayList<EducationItem>();
        jdbcTemplate.query(sqlManager.getSQL("EducationDAO/selectAllEducationItems"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(EducationItem.class, rs));
            }
        });
        return pList;
    }
}
