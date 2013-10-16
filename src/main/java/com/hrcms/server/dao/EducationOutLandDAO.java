package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.EducationOutLandItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class EducationOutLandDAO extends baseDAO {
    public List<EducationOutLandItem> getEducationOutLandItemList() throws Exception {

        final List<EducationOutLandItem> pList = new ArrayList<EducationOutLandItem>();
        jdbcTemplate.query(sqlManager.getSQL("EducationOutLandDAO/sql_all_items"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(EducationOutLandItem.class, rs));
            }
        });
        return pList;
    }
}
