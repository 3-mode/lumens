package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.PersonSummaryListRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class PersonSummaryListDAO extends baseDAO {
    public List<PersonSummaryListRecord> getPersonSummaryRecordList() throws Exception {
        final List<PersonSummaryListRecord> pList = new ArrayList<PersonSummaryListRecord>();
        jdbcTemplate.query(PersonSummaryListRecord.SQL_ALL_SUMMARY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(PersonSummaryListRecord.class, rs));
            }
        });
        return pList;
    }
}
