package com.hrcms.server.dao;

import com.hrcms.server.dao.factory.EntityFactory;
import com.hrcms.server.model.Evaluation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

public class EvaluationDAO extends baseDAO {
    public List<Evaluation> getEvaluationItemList() throws Exception {

        final List<Evaluation> pList = new ArrayList<Evaluation>();
        jdbcTemplate.query(sqlManager.getSQL("EvaluationDAO/sql_all_evaluations"), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                pList.add(EntityFactory.createEntity(Evaluation.class, rs));
            }
        });
        return pList;
    }
}
