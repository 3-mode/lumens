package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.Punishment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class PunishmentDAO extends baseDAO {
    public List<Punishment> getPunishmentList() throws Exception {
        final List<Punishment> pList = new ArrayList<Punishment>();
        jdbcTemplate.query(sqlManager.getSQL("PunishmentDAO/sql_all_punishments"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Punishment.class, rs));
            }
        });
        return pList;
    }
}
