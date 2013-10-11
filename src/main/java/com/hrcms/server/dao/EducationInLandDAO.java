package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.EducationInLandItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class EducationInLandDAO extends baseDAO {
    public List<EducationInLandItem> getEducationInLandItemList() throws Exception {

        final List<EducationInLandItem> pList = new ArrayList<EducationInLandItem>();
        jdbcTemplate.query(sqlManager.getSQL("EducationInLandDAO/sql_all_educations"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(EducationInLandItem.class, rs));
            }
        });
        return pList;
    }
}
