package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.AwardItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class AwardDAO extends baseDAO {
    public List<AwardItem> getAwardItemList() throws Exception {
        final List<AwardItem> pList = new ArrayList<AwardItem>();
        jdbcTemplate.query(sqlManager.getSQL("AwardDAO/sql_all_items"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(AwardItem.class, rs));
            }
        });
        return pList;
    }
}
