package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.FamilyMember;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class FamilyMemberDAO extends baseDAO {
    public List<FamilyMember> getFamilyMemberList() throws Exception {

        final List<FamilyMember> pList = new ArrayList<FamilyMember>();
        jdbcTemplate.query(sqlManager.getSQL("FamilyMemberDAO/sql_all_members"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(FamilyMember.class, rs));
            }
        });
        return pList;
    }
}
